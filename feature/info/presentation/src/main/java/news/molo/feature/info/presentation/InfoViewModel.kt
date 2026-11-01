package news.molo.feature.info.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.navigation.Screen
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.usecase.apperance.ObserveColorSchemaUseCase
import news.molo.domain.usecase.apperance.SetColorSchemaUseCase
import news.molo.domain.usecase.user.DeleteSelfUserUseCase
import news.molo.domain.usecase.user.LogoutUseCase
import news.molo.domain.usecase.user.ObserveIsUserLoggedInUseCase

class InfoViewModel(
    observeIsLoggedInUseCase: ObserveIsUserLoggedInUseCase,
    observeColorSchemaUseCase: ObserveColorSchemaUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteSelfUserUseCase: DeleteSelfUserUseCase,
    private val setColorSchemaUseCase: SetColorSchemaUseCase
) : BaseViewModel<InfoState, InfoState.Intent>(InfoState()) {

    private val isLoggedIn = observeIsLoggedInUseCase()
        .onEach { isLoggedIn ->
            setState(currentState.copy(isLoggedIn = isLoggedIn))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val colorSchema = observeColorSchemaUseCase()
        .onEach { colorSchema ->
            setState(currentState.copy(colorSchemaPreference = colorSchema))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: InfoState.Intent) = when (intent) {
        is InfoState.Intent.DeleteAccount -> handleIntent<_, _>(
            intent = intent,
            handler = ::handleAccountDeletion
        )

        is InfoState.Intent.Logout -> handleIntent<_, _>(
            intent = intent,
            handler = ::handleLogout
        )

        InfoState.Intent.DismissDeleteAccountAlert -> reduceState<InfoState> {
            copy(isDeleteAccountAlertVisible = false)
        }

        InfoState.Intent.ShowDeleteAccountAlert -> reduceState<InfoState> {
            copy(isDeleteAccountAlertVisible = true)
        }

        is InfoState.Intent.SetColorSchema -> handleIntent<_, _>(
            intent = intent,
            handler = ::handleSetColorSchema
        )

        InfoState.Intent.ToOrganisationSummary -> dispatchNavigationEvent(
            NavigationEvent.NavigateTo(destination = Screen.OrganizationSummary)
        )
    }

    private fun handleLogout(
        state: InfoState,
        intent: InfoState.Intent.Logout,
    ) {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    private fun handleAccountDeletion(
        state: InfoState,
        intent: InfoState.Intent.DeleteAccount,
    ) {
        setState(state.copy(isDeleteAccountAlertVisible = false))
        viewModelScope.launch {
            deleteSelfUserUseCase().collectOutcome(
                onFailure = { showSnackbar(it.message) }
            )
        }
    }

    private fun handleSetColorSchema(
        state: InfoState,
        intent: InfoState.Intent.SetColorSchema,
    ) {
        viewModelScope.launch {
            setColorSchemaUseCase(intent.colorSchemaPreference)
        }
    }
}