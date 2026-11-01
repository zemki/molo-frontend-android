package news.molo.feature.auth.reset.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import kotlinx.coroutines.launch
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.usecase.user.ResetPasswordUseCase

class PasswordResetViewModel(
    private val token: String,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel<PasswordResetState, PasswordResetState.Intent>(
    PasswordResetState.Default()
) {

    override fun executeIntent(intent: PasswordResetState.Intent) {
        when (intent) {
            is PasswordResetState.Default.Intent.SetPassword -> reduceState<PasswordResetState.Default> {
                copy(password = intent.value)
            }

            is PasswordResetState.Default.Intent.SetRepeatPassword -> reduceState<PasswordResetState.Default> {
                copy(repeatPassword = intent.value)
            }

            is PasswordResetState.Default.Intent.RequestReset -> handleIntent<_, _>(
                intent,
                ::handleRequestReset
            )

            is PasswordResetState.Default.Intent.Retry -> reduceState<PasswordResetState.Default> {
                copy(errorMessage = null)
            }

            PasswordResetState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
        }
    }

    private fun handleRequestReset(
        state: PasswordResetState.Default,
        intent: PasswordResetState.Default.Intent.RequestReset,
    ) {
        viewModelScope.launch {

            if (!state.doesPasswordMatch) {
                setState(state.copy(errorMessage = "Passwörter stimmen nicht überein"))
                return@launch
            }

            resetPasswordUseCase(
                ResetPasswordUseCase.Params(
                    token = token,
                    password = state.password
                )
            )
                .collectOutcome(
                    onProgress = { setState(PasswordResetState.Loading) },
                    onFailure = { showSnackbar(it.message) },
                    onSuccess = { setState(state.copy(emailRequested = true)) },
                )
        }
    }
}