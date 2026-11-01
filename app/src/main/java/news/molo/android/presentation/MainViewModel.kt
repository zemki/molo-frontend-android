package news.molo.android.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent.*
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import news.molo.core.navigation.Screen.*
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.SearchHistoryEntry
import news.molo.domain.model.SelfUserData
import news.molo.domain.usecase.apperance.ObserveColorSchemaUseCase
import news.molo.domain.usecase.area.ObserveAreaAndRadiusSelectionUseCase
import news.molo.domain.usecase.onboarding.ObserveOnboardingCompletionUseCase
import news.molo.domain.usecase.search.DeleteSearchHistoryEntryUseCase
import news.molo.domain.usecase.search.DeleteWholeSearchHistoryUseCase
import news.molo.domain.usecase.search.ObserveSearchHistoryUseCase
import news.molo.domain.usecase.search.StoreSearchHistoryEntryUseCase
import news.molo.domain.usecase.user.*

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel(
    observeOnboardingCompletionUseCase: ObserveOnboardingCompletionUseCase,
    observeColorSchemaUseCase: ObserveColorSchemaUseCase,
    observeAreaAndRadiusSelectionUseCase: ObserveAreaAndRadiusSelectionUseCase,
    observeSelfUserUseCase: ObserveSelfUserUseCase,
    postNewPushTokenUseCase: PostNewPushTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val setBearerUseCase: SetBearerUseCase,
    observeSearchHistoryUseCase: ObserveSearchHistoryUseCase,
    private val deleteSearchHistoryEntryUseCase: DeleteSearchHistoryEntryUseCase,
    private val deleteWholeSearchHistoryUseCase: DeleteWholeSearchHistoryUseCase,
    private val storeSearchHistoryEntryUseCase: StoreSearchHistoryEntryUseCase
) : BaseViewModel<AppState, AppState.Intent>(AppState()) {

    private val onboardingCompleted = observeOnboardingCompletionUseCase()
        .onEach { isComplete ->
            val state = when (isComplete) {
                true -> OnboardingCompletionState.Complete
                false -> OnboardingCompletionState.NotComplete
            }
            setState(currentState.copy(onboardingCompletionState = state))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val colorSchema = observeColorSchemaUseCase()
        .onEach { setState(currentState.copy(colorSchemaPreference = it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val selectedAreaWithRadius = observeAreaAndRadiusSelectionUseCase()
        .onEach { selectedLocation -> setState(currentState.copy(selectedLocation = selectedLocation)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val isLoggedIn = observeSelfUserUseCase()
        .onEachOutcome(
            onFailure = { setState(currentState.copy(isLoggedIn = false)) },
            onSuccess = { user ->
                setState(
                    currentState.copy(
                        isLoggedIn = true,
                        selfUserData = SelfUserData(
                            organizationId = user.organizationId,
                            avatarUrl = user.imageUrl.orEmpty(),
                            username = user.displayName
                        )
                    )
                )
            }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        viewModelScope.launch {
            postNewPushTokenUseCase().collectOutcome()
        }

        viewModelScope.launch {
            collectStateFlow()
                .map { it.query }
                .distinctUntilChanged()
                .flatMapLatest { observeSearchHistoryUseCase(it) }
                .collectLatest {
                    setState(currentState.copy(searchHistory = it))
                }
        }
    }

    override fun executeIntent(intent: AppState.Intent) {
        when (intent) {
            is AppState.Intent.Logout -> handleLogoutIntent()
            is AppState.Intent.HideCurrentSheet -> setState(currentState.copy(bottomSheetState = BottomSheetState.Hidden))
            is AppState.Intent.ShowLogin -> setState(currentState.copy(bottomSheetState = BottomSheetState.Login))
            AppState.Intent.ToArticlePostCreation -> dispatchNavigationEvent(
                NavigateTo(
                    ArticleCreation()
                )
            )

            AppState.Intent.ToEventPostCreation -> dispatchNavigationEvent(
                NavigateTo(
                    EventCreation()
                )
            )

            is AppState.Intent.SetBearer -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleSetBearerIntent
            )

            is AppState.Intent.NavigateToArticle -> dispatchNavigationEvent(
                NavigateTo(
                    ArticleDetails(intent.articleId)
                )
            )

            is AppState.Intent.NavigateToEvent -> dispatchNavigationEvent(
                NavigateTo(
                    EventDetails(intent.eventId)
                )
            )

            is AppState.Intent.NavigateToSearchResults -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleNavigateToSearchResults
            )

            is AppState.Intent.DeleteSearchHistory -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleDeleteSearchHistory
            )

            is AppState.Intent.DeleteSearchHistoryEntry -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleDeleteSearchHistoryEntry
            )

            is AppState.Intent.SetQuery -> reduceState<AppState> { copy(query = intent.query) }
        }
    }

    private fun handleNavigateToSearchResults(
        state: AppState,
        intent: AppState.Intent.NavigateToSearchResults
    ) {
        viewModelScope.launch {
            storeSearchHistoryEntryUseCase(
                SearchHistoryEntry(
                    query = intent.query,
                    type = intent.type
                )
            )

            dispatchNavigationEvent(
                NavigateTo(
                    SearchResults(intent.query, intent.selectedTab)
                )
            )
        }
    }

    private fun handleDeleteSearchHistory(
        state: AppState,
        intent: AppState.Intent.DeleteSearchHistory
    ) {
        viewModelScope.launch {
            deleteWholeSearchHistoryUseCase()
        }
    }

    private fun handleDeleteSearchHistoryEntry(
        state: AppState,
        intent: AppState.Intent.DeleteSearchHistoryEntry
    ) {
        viewModelScope.launch {
            deleteSearchHistoryEntryUseCase(intent.id)
        }
    }


    private fun handleLogoutIntent() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    private fun handleSetBearerIntent(
        state: AppState,
        intent: AppState.Intent.SetBearer
    ) {
        viewModelScope.launch {
            setBearerUseCase(intent.bearer)
        }
    }
}