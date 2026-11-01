package news.molo.feature.auth.login.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.navigation.Screen
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.usecase.user.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginState, LoginState.Intent>(
    LoginState.Default(),
    useEventBus = false
) {
    override fun executeIntent(intent: LoginState.Intent) {
        when (intent) {
            is LoginState.Default.Intent.SetEmail -> reduceState<LoginState.Default> {
                copy(
                    email = intent.value,
                    errorMessage = null
                )
            }

            is LoginState.Default.Intent.SetPassword -> reduceState<LoginState.Default> {
                copy(
                    password = intent.value,
                    errorMessage = null
                )
            }

            is LoginState.Default.Intent.Login -> handleIntent<_, _>(intent, ::handleLogin)
            is LoginState.Default.Intent.Retry -> reduceState<LoginState.Default> {
                copy(
                    errorMessage = null
                )
            }

            LoginState.Intent.ToPasswordReset -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(
                    Screen.PasswordReset
                )
            )

            LoginState.Intent.ToRegistration -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(
                    Screen.Registration
                )
            )
        }
    }

    private fun handleLogin(
        state: LoginState.Default,
        intent: LoginState.Default.Intent.Login,
    ) {
        viewModelScope.launch {
            loginUseCase(LoginUseCase.Params(state.email, state.password))
                .collectOutcome(
                    onProgress = { setState(LoginState.Loading) },
                    onFailure = {
                        setState(state.copy(errorMessage = "Bitte überprüfe deine E-Mail und Passwort"))
                    },
                    onSuccess = {
                        setState(state.copy(email = "", password = ""))
                        sendUIEvent(UIEvent.NavigateBack)
                    }
                )
        }
    }
}