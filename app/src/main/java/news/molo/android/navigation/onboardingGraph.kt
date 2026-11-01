package news.molo.android.navigation

import androidx.navigation.NavGraphBuilder
import news.molo.core.navigation.OnboardingScreen
import news.molo.feature.onboarding.ui.OnboardingScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.onboardingGraph() {
    composableWithDetailsTransition<OnboardingScreen.Onboarding> {
        OnboardingScreen(koinViewModel())
    }
}