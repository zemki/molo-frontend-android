package news.molo.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.navigation.Screen
import news.molo.core.viewmodel.BaseViewModelWithNavResult
import news.molo.domain.model.User
import news.molo.domain.usecase.user.ObserveSelfUserUseCase
import news.molo.domain.usecase.user.UpdateUserProfileUseCase

class ProfileViewModel(
    getSelfUserUseCase: ObserveSelfUserUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase,
) : BaseViewModelWithNavResult<ProfileState, ProfileState.Intent, ProfileNavResult>(
    initialState = ProfileState.Loading,
    useEventBus = false
) {

    private val selfUser = getSelfUserUseCase()
        .onEachOutcome(
            onProgress = { setState(ProfileState.Loading) },
            onFailure = { showSnackbar(it.message) },
            onSuccess = { setState(ProfileState.Loaded(it, it)) }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: ProfileState.Intent) {
        when (intent) {
            is ProfileState.Loaded.Intent.SetDescription -> updateUser { copy(description = intent.value) }
            is ProfileState.Loaded.Intent.SetDisplayName -> updateUser { copy(displayName = intent.value) }
            is ProfileState.Loaded.Intent.SetFirstName -> updateUser { copy(firstName = intent.value) }
            is ProfileState.Loaded.Intent.SetHomepage -> updateUser { copy(homepage = intent.value) }
            is ProfileState.Loaded.Intent.SetLastName -> updateUser { copy(lastName = intent.value) }
            is ProfileState.Loaded.Intent.SetMail -> updateUser { copy(mail = intent.value) }
            is ProfileState.Loaded.Intent.SetPhone -> updateUser { copy(phone = intent.value) }
            is ProfileState.Loaded.Intent.SetImage -> updateUser {
                copy(imageUrl = intent.value, imageSource = intent.source)
            }

            is ProfileState.Loaded.Intent.SaveChanges -> handleIntent<_, _>(
                intent,
                ::handleSaveChangesIntent
            )

            ProfileState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            ProfileState.Loaded.Intent.NavigateToAreaSelection -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(destination = Screen.AreaSelection)
            )
        }
    }

    private fun handleSaveChangesIntent(
        state: ProfileState.Loaded,
        intent: ProfileState.Loaded.Intent.SaveChanges,
    ) {
        viewModelScope.launch {
            updateUserProfile(
                UpdateUserProfileUseCase.Params(
                    originalUser = state.originalUser,
                    user = state.user
                )
            ).collectOutcome(
                onProgress = { setState(ProfileState.Loading) },
                onFailure = { showSnackbar(it.message) },
                onSuccess = { setState(state.copy(originalUser = state.user, user = state.user)) }
            )
        }
    }

    private inline fun updateUser(crossinline update: User.() -> User) {
        reduceState<ProfileState.Loaded> {
            copy(user = user.update())
        }
    }

    override fun onNavResultReceived(event: ProfileNavResult) {
        when (event) {
            is ProfileNavResult.SelectedArea -> reduceState<ProfileState.Loaded> {
                copy(user = user.copy(area = event.value))
            }
        }
    }
}