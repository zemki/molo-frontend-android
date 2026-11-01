package news.molo.feature.auth.reset.request.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import kotlinx.coroutines.launch
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.usecase.user.RequestPasswordResetUseCase

class RequestPasswordResetViewModel(
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase
) : BaseViewModel<RequestPasswordResetState, RequestPasswordResetState.Intent>(
    RequestPasswordResetState.Default()
) {

    override fun executeIntent(intent: RequestPasswordResetState.Intent) {
        when (intent) {
            is RequestPasswordResetState.Default.Intent.SetEmail -> reduceState<RequestPasswordResetState.Default> {
                copy(email = intent.value)
            }

            is RequestPasswordResetState.Default.Intent.RequestReset -> handleIntent<_, _>(
                intent,
                ::handleRequestReset
            )

            is RequestPasswordResetState.Default.Intent.Retry -> reduceState<RequestPasswordResetState.Default> {
                copy(errorMessage = null)
            }

            RequestPasswordResetState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
        }
    }

    private fun handleRequestReset(
        state: RequestPasswordResetState.Default,
        intent: RequestPasswordResetState.Default.Intent.RequestReset,
    ) {
        viewModelScope.launch {
            setState(RequestPasswordResetState.Loading)

            requestPasswordResetUseCase(state.email)
                .collectOutcome(
                    onProgress = { setState(RequestPasswordResetState.Loading) },
                    onFailure = { setState(state.copy(errorMessage = "Etwas ist schief gelaufen")) },
                    onSuccess = { setState(state.copy(emailRequested = true)) }
                )
        }
    }
}