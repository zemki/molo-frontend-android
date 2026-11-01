package news.molo.core.navigation

import kotlinx.serialization.Serializable

sealed interface OnboardingScreen: Screen {

    @Serializable
    data object Graph : Screen

    @Serializable
    data object Onboarding : Screen
}