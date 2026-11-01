package news.molo.feature.onboarding.ui


import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.OnboardingStep
import news.molo.feature.onboarding.presentation.OnboardingState
import news.molo.feature.onboarding.ui.page.PageFour
import news.molo.feature.onboarding.ui.page.PageFourWithoutPush
import news.molo.feature.onboarding.ui.page.PageOne
import news.molo.feature.onboarding.ui.page.PageThree
import news.molo.feature.onboarding.ui.page.PageTwo

@Composable
internal fun OnboardingPage(
    state: OnboardingState,
) {
    Crossfade(state.currentStep) { currentStep ->
        PageTemplateWithPaddingSlot { contentPadding ->
            when (currentStep) {
                OnboardingStep.First -> PageOne(contentPadding)
                OnboardingStep.Second -> PageTwo(contentPadding)
                OnboardingStep.Third -> PageThree(contentPadding)
                OnboardingStep.Fourth -> PageFour(contentPadding)
                OnboardingStep.FourthWithoutPush -> PageFourWithoutPush(contentPadding)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        OnboardingPage(
            state = OnboardingState(),
        )
    }
}