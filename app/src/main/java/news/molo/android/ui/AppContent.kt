package news.molo.android.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import de.convelop.forge.navigation.NavigationEvent
import news.molo.android.navigation.topLevelGraph
import news.molo.android.presentation.AppState
import news.molo.android.presentation.BottomSheetState
import news.molo.core.navigation.TopLevelScreen
import news.molo.feature.auth.login.ui.LoginBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AppContent(
    navController: NavHostController,
    executeIntent: (AppState.Intent) -> Unit,
    bottomSheetState: BottomSheetState,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = TopLevelScreen.Feed,
    ) {
        topLevelGraph(onRequestLogin = { executeIntent(AppState.Intent.ShowLogin) })
    }

    when (bottomSheetState) {
        BottomSheetState.Hidden -> Unit
        BottomSheetState.Login -> LoginBottomSheet(
            viewModel = koinViewModel(),
            onDismiss = { executeIntent(AppState.Intent.HideCurrentSheet) },
            onNavigationAction = {
                executeIntent(AppState.Intent.HideCurrentSheet)
                onNavigationEvent(it)
            }
        )

        is BottomSheetState.ResetPassword -> {}
    }
}