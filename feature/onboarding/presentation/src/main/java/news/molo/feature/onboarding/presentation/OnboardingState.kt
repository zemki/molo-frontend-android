package news.molo.feature.onboarding.presentation

import android.os.Build
import news.molo.core.resource.R
import news.molo.domain.model.OnboardingStep
import news.molo.domain.model.OnboardingStep.Fourth
import news.molo.domain.model.OnboardingStep.FourthWithoutPush
import news.molo.domain.model.OnboardingStep.entries

data class OnboardingState(
    val steps: List<OnboardingStep> = entriesForSDK(),
    val currentStep: OnboardingStep = steps.first(),
) {

    companion object {
        /** [OnboardingStep.Fourth] is designed to request the permission [android.Manifest.permission.POST_NOTIFICATIONS]
         *  The permission does not exist on versions older than [Build.VERSION_CODES.TIRAMISU].
         *  That's why we only show it for never versions and use a fallback for the others
         **/
        fun entriesForSDK(): List<OnboardingStep> {
            return entries.filter {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it != FourthWithoutPush
                } else {
                    it != Fourth
                }
            }
        }
    }

    sealed interface Intent {
        data object Next : Intent
        data class SetStep(val step: OnboardingStep) : Intent
        data class PermissionResult(val isGranted: Boolean) : Intent
        data object Previous : Intent
    }

    val lastProgressIndex = steps.count() - 1
    val currentIndex: Int = steps.indexOf(currentStep)

    val progress: Float = currentIndex.toFloat() / steps.lastIndex.toFloat()

    val isFirstStep: Boolean = currentIndex == 0
    val isLastStep: Boolean = currentIndex == steps.lastIndex

    val currentStepNumber: Int = currentIndex + 1
    val totalSteps: Int = steps.size

    val isProgressVisible: Boolean = !isLastStep

    val titleResForStep: Int = when (currentStep) {
        OnboardingStep.First -> R.string.news_molo_onboarding_step1_next
        OnboardingStep.Second -> R.string.news_molo_onboarding_step2_next
        OnboardingStep.Third -> R.string.news_molo_onboarding_step3_next
        else -> R.string.news_molo_onboarding_step4_next
    }
}