package news.molo.feature.onboarding.presentation

import android.os.Build
import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent.*
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.navigation.Screen.*
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.OnboardingStep
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.onboarding.MarkOnboardingAsCompletedUseCase

class OnboardingViewModel(
    private val markOnboardingAsCompletedUseCase: MarkOnboardingAsCompletedUseCase
) : BaseViewModel<OnboardingState, OnboardingState.Intent>(OnboardingState(), useEventBus = false) {

    override fun executeIntent(intent: OnboardingState.Intent) = when (intent) {
        is OnboardingState.Intent.Next -> handleIntent<_, _>(
            intent = intent,
            handler = ::handleNextIntent
        )

        is OnboardingState.Intent.Previous -> handleIntent<_, _>(
            intent = intent,
            handler = ::handlePreviousIntent
        )

        is OnboardingState.Intent.SetStep -> setState(currentState.copy(currentStep = intent.step))
        is OnboardingState.Intent.PermissionResult -> handleIntent<_, _>(
            intent = intent,
            handler = ::handlePermissionResult
        )
    }

    private fun handleNextIntent(
        state: OnboardingState,
        intent: OnboardingState.Intent.Next,
    ) {

        viewModelScope.launch {
            if (state.currentStep == OnboardingStep.Fourth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    sendUIEvent(UIEvent.RequestNotificationPermission)
                    return@launch
                }
            }

            handleNextOnboardingStep(state)
        }
    }

    private fun handlePermissionResult(
        state: OnboardingState,
        intent: OnboardingState.Intent.PermissionResult,
    ) {
        viewModelScope.launch { handleNextOnboardingStep(state) }
    }

    private suspend fun handleNextOnboardingStep(state: OnboardingState) {
        if (state.isLastStep) {
            markOnboardingAsCompletedUseCase()
            return
        }

        val nextStep = state.steps[state.currentIndex + 1]

        when (nextStep) {
            OnboardingStep.Third -> dispatchNavigationEvent(
                NavigateTo(
                    LocationSelection(
                        ConfigurationType.Onboarding
                    )
                )
            )

            OnboardingStep.Fourth, OnboardingStep.FourthWithoutPush -> dispatchNavigationEvent(
                NavigateTo(
                    TagSelection(
                        defaultTagSelectionType = TabSelectionType.Article,
                        tagConfigurationType = ConfigurationType.Onboarding
                    )
                )
            )

            OnboardingStep.First, OnboardingStep.Second -> {/* Nothing to do here */
            }
        }
        setState(currentState.copy(currentStep = state.steps[state.currentIndex + 1]))
    }

    private fun handlePreviousIntent(
        state: OnboardingState,
        intent: OnboardingState.Intent.Previous,
    ) {
        if (state.isFirstStep) {
            return
        }

        setState(currentState.copy(currentStep = state.steps[state.currentIndex - 1]))
    }
}