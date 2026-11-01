package news.molo.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import de.convelop.forge.compose.navigation.nav2.CollectNavigationEvents
import de.convelop.forge.navigation.NavigationDispatcher
import kotlinx.coroutines.launch
import news.molo.android.presentation.AppState
import news.molo.android.presentation.OnboardingCompletionState
import news.molo.android.ui.MainContent
import news.molo.core.designsystem.theme.AppTheme
import news.molo.core.designsystem.theme.LocalIsDarkTheme
import news.molo.core.navigation.OnboardingScreen
import news.molo.core.navigation.Screen
import news.molo.core.navigation.TopLevelScreen
import news.molo.feature.auth.reset.ui.PasswordResetScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation(
    state: AppState,
    executeIntent: (AppState.Intent) -> Unit,
) {
    val rootNavController = rememberNavController()
    val bottomNavController = rememberNavController()
    val navigationDispatcher = koinInject<NavigationDispatcher>()
    val scope = rememberCoroutineScope()
    rootNavController.CollectNavigationEvents(dispatcher = navigationDispatcher)

    AppTheme(darkTheme = LocalIsDarkTheme.current) {
        NavHost(
            navController = rootNavController,
            startDestination = if (state.onboardingCompletionState == OnboardingCompletionState.NotComplete) OnboardingScreen.Graph else TopLevelScreen.Graph(),
            popExitTransition = DefaultNavTransitions.popExitTransition,
            popEnterTransition = DefaultNavTransitions.popEnterTransition

        ) {
            composable<TopLevelScreen.Graph>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = Deeplinks.AFTER_REGISTRATION
                    }
                ),
            ) {
                val bearer = it.toRoute<TopLevelScreen.Graph>().bearer

                MainContent(
                    bearer = bearer,
                    rootNavController = rootNavController,
                    bottomNavController = bottomNavController,
                    appState = state,
                    executeIntent = executeIntent,
                    onNavigationEvent = {
                        scope.launch {
                            navigationDispatcher.dispatch(it)
                        }
                    }
                )
            }

            navigation<Screen.Graph>(startDestination = Screen.Profile) {
                appGraph(rootNavController)
            }

            navigation<Screen.PasswordResetGraph>(startDestination = Screen.PasswordReset(token = "")) {
                composableWithDetailsTransition<Screen.PasswordReset>(
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = Deeplinks.PASSWORD_RESET
                        }
                    ),
                ) { backStackEntry ->
                    val token = backStackEntry.toRoute<Screen.PasswordReset>().token

                    PasswordResetScreen(
                        viewModel = koinViewModel(parameters = {
                            parametersOf(token)
                        })
                    )
                }
            }

            navigation<OnboardingScreen.Graph>(startDestination = OnboardingScreen.Onboarding) {
                onboardingGraph()
            }
        }
    }
}
