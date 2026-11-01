package news.molo.feature.organization.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.navigation.NavigationEvent.*
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.navigation.Screen
import news.molo.core.util.paging.createPager
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.Article
import news.molo.domain.model.Event
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.article.DeletePostedArticleUseCase
import news.molo.domain.usecase.article.GetMyPaginatedArticlePostsUseCase
import news.molo.domain.usecase.article.GetPaginatedArticlesForOrganizationUseCase
import news.molo.domain.usecase.event.DeletePostedEventUseCase
import news.molo.domain.usecase.event.GetMyPaginatedEventPostsUseCase
import news.molo.domain.usecase.event.GetPaginatedEventsForOrganizationUseCase
import news.molo.domain.usecase.organization.ObserveOrganizationUseCase
import news.molo.domain.usecase.posting.ObservePostingReloadTriggerUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DetailedOrganizationViewModel(
    organizationId: Int,
    private val tabSelectionType: TabSelectionType,
    observeOrganizationUseCase: ObserveOrganizationUseCase,
    getMyPaginatedEventPostsUseCase: GetMyPaginatedEventPostsUseCase,
    getMyPaginatedEventsForOrganizationUseCase: GetPaginatedEventsForOrganizationUseCase,
    private val deletePostedEventUseCase: DeletePostedEventUseCase,
    getMyPaginatedArticlePostsUseCase: GetMyPaginatedArticlePostsUseCase,
    getPaginatedArticlesForOrganizationUseCase: GetPaginatedArticlesForOrganizationUseCase,
    private val deletePostedArticleUseCase: DeletePostedArticleUseCase,
    observePostingReloadTriggerUseCase: ObservePostingReloadTriggerUseCase
) : BaseViewModel<DetailedOrganizationState, DetailedOrganizationState.Intent>(
    DetailedOrganizationState.Loading
) {

    private val organisationOwnedBySelfUser = MutableStateFlow(false)

    private val organization = observeOrganizationUseCase(organizationId)
        .onEachOutcome(
            onProgress = { setState(DetailedOrganizationState.Loading) },
            onFailure = { dispatchNavigationEvent(NavigateUp) },
            onSuccess = { organization ->
                organisationOwnedBySelfUser.tryEmit(organization.isOwnedBySelfUser)
                setState(
                    DetailedOrganizationState.Loaded(
                        organization,
                        selectedTab = tabSelectionType
                    )
                )
            }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val deletedEventIds = MutableStateFlow<Set<Int>>(emptySet())

    private val sourceEvents = organisationOwnedBySelfUser
        .flatMapLatest { isOwnedBySelfUser -> observePostingReloadTriggerUseCase().map { isOwnedBySelfUser } }
        .flatMapLatest { isOwnedBySelfUser ->
            when (isOwnedBySelfUser) {
                true -> createPager(
                    getMyPaginatedEventPostsUseCase,
                    paramsFactory = { pageSize, offset ->
                        GetMyPaginatedEventPostsUseCase.Params(
                            pageSize = pageSize,
                            offset = offset
                        )
                    }).flow

                false -> createPager(
                    getMyPaginatedEventsForOrganizationUseCase,
                    paramsFactory = { pageSize, offset ->
                        GetPaginatedEventsForOrganizationUseCase.Params(
                            pageSize = pageSize,
                            offset = offset,
                            organisationId = organizationId
                        )
                    }).flow
            }
        }
        .cachedIn(viewModelScope)

    val eventPagingData: Flow<PagingData<Event>> = sourceEvents
        .combine(deletedEventIds) { pagingData, idsToFilter ->
            pagingData.filter { article ->
                article.id !in idsToFilter
            }
        }

    private val deletedArticleIds = MutableStateFlow<Set<Int>>(emptySet())

    private val sourceArticle = organisationOwnedBySelfUser
        .flatMapLatest { isOwnedBySelfUser -> observePostingReloadTriggerUseCase().map { isOwnedBySelfUser } }
        .flatMapLatest { isOwnedBySelfUser ->
            when (isOwnedBySelfUser) {
                true -> createPager(
                    getMyPaginatedArticlePostsUseCase,
                    paramsFactory = { pageSize, offset ->
                        GetMyPaginatedArticlePostsUseCase.Params(
                            pageSize = pageSize,
                            offset = offset
                        )
                    }).flow

                false -> createPager(
                    getPaginatedArticlesForOrganizationUseCase,
                    paramsFactory = { pageSize, offset ->
                        GetPaginatedArticlesForOrganizationUseCase.Params(
                            pageSize = pageSize,
                            offset = offset,
                            organisationId = organizationId
                        )
                    }).flow
            }
        }
        .cachedIn(viewModelScope)

    val articlePagingData: Flow<PagingData<Article>> = sourceArticle
        .combine(deletedArticleIds) { pagingData, idsToFilter ->
            pagingData.filter { article ->
                article.id !in idsToFilter
            }
        }

    override fun executeIntent(intent: DetailedOrganizationState.Intent) {
        when (intent) {
            is DetailedOrganizationState.Loaded.Intent.SetSelectedTab -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleSetSelectedTab
            )

            is DetailedOrganizationState.Loaded.Intent.ToArticle -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToArticle
            )

            is DetailedOrganizationState.Loaded.Intent.ToEvent -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToEvent
            )

            DetailedOrganizationState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            DetailedOrganizationState.Loaded.Intent.ToProfile -> dispatchNavigationEvent(
                NavigateTo(
                    Screen.Profile
                )
            )

            is DetailedOrganizationState.Loaded.Intent.DeleteArticle -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleDeleteArticleIntent
            )

            is DetailedOrganizationState.Loaded.Intent.DeleteEvent -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleDeleteEventIntent
            )
        }
    }

    private fun handleSetSelectedTab(
        state: DetailedOrganizationState.Loaded,
        intent: DetailedOrganizationState.Loaded.Intent.SetSelectedTab,
    ) {
        setState(state.copy(selectedTab = intent.tab))
    }

    private fun handleToArticle(
        state: DetailedOrganizationState.Loaded,
        intent: DetailedOrganizationState.Loaded.Intent.ToArticle
    ) {
        val screenToNavigate = when (state.organization.isOwnedBySelfUser) {
            true -> Screen.ArticleCreation(articleId = intent.articleId)
            false -> Screen.ArticleDetails(articleId = intent.articleId)
        }

        dispatchNavigationEvent(NavigationEvent.NavigateTo(screenToNavigate))
    }

    private fun handleToEvent(
        state: DetailedOrganizationState.Loaded,
        intent: DetailedOrganizationState.Loaded.Intent.ToEvent
    ) {
        val screenToNavigate = when (state.organization.isOwnedBySelfUser) {
            true -> Screen.EventCreation(eventId = intent.eventId)
            false -> Screen.EventDetails(eventId = intent.eventId)
        }

        dispatchNavigationEvent(NavigateTo(screenToNavigate))
    }

    private fun handleDeleteEventIntent(
        state: DetailedOrganizationState.Loaded,
        intent: DetailedOrganizationState.Loaded.Intent.DeleteEvent
    ) {

        viewModelScope.launch {
            deletedEventIds.value += intent.eventId

            deletePostedEventUseCase(intent.eventId)
                .collectOutcome(
                    onFailure = {
                        showSnackbar(it.message)
                        deletedEventIds.value -= intent.eventId
                    }
                )
        }
    }

    private fun handleDeleteArticleIntent(
        state: DetailedOrganizationState.Loaded,
        intent: DetailedOrganizationState.Loaded.Intent.DeleteArticle
    ) {

        viewModelScope.launch {
            deletedArticleIds.value += intent.articleId

            deletePostedArticleUseCase(intent.articleId)
                .collectOutcome(
                    onFailure = {
                        showSnackbar(it.message)
                        deletedArticleIds.value -= intent.articleId
                    }
                )
        }
    }
}