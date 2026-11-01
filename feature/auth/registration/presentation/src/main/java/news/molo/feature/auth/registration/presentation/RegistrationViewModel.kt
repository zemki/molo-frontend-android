package news.molo.feature.auth.registration.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import kotlinx.coroutines.launch
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.UserRegistration
import news.molo.domain.usecase.user.RegisterUserUseCase

class RegistrationViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : BaseViewModel<RegistrationState, RegistrationState.Intent>(
    RegistrationState.Default()
) {
    override fun executeIntent(intent: RegistrationState.Intent) {
        when (intent) {
            is RegistrationState.Default.Intent.SetFirstName -> reduceState<RegistrationState.Default> {
                copy(
                    firstName = intent.value
                )
            }

            is RegistrationState.Default.Intent.SetLastName -> reduceState<RegistrationState.Default> {
                copy(
                    lastName = intent.value
                )
            }

            is RegistrationState.Default.Intent.SetDisplayName -> reduceState<RegistrationState.Default> {
                copy(
                    displayName = intent.value
                )
            }

            is RegistrationState.Default.Intent.SetEmail -> reduceState<RegistrationState.Default> {
                copy(
                    email = intent.value
                )
            }

            is RegistrationState.Default.Intent.SetPassword -> reduceState<RegistrationState.Default> {
                copy(
                    password = intent.value
                )
            }

            is RegistrationState.Default.Intent.Register -> handleIntent<_, _>(
                intent,
                ::handleRegistration
            )

            is RegistrationState.Default.Intent.Retry -> reduceState<RegistrationState.Default> {
                copy(
                    errorMessage = null
                )
            }

            is RegistrationState.Default.Intent.SetAgbAccepted -> reduceState<RegistrationState.Default> {
                copy(
                    agbAccepted = intent.value
                )
            }

            RegistrationState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
        }
    }

    private fun handleRegistration(
        state: RegistrationState.Default,
        intent: RegistrationState.Default.Intent.Register,
    ) {
        viewModelScope.launch {
            registerUserUseCase(
                UserRegistration(
                    email = state.email,
                    password = state.password,
                    firstName = state.firstName,
                    lastName = state.lastName,
                    displayName = state.displayName,
                )
            ).collectOutcome(
                onProgress = { setState(RegistrationState.Loading) },
                onFailure = { setState(state.copy(errorMessage = "Bei der Registrierung kam es zu einem Fehler")) },
                onSuccess = { setState(RegistrationState.Done) }
            )
        }
    }
}