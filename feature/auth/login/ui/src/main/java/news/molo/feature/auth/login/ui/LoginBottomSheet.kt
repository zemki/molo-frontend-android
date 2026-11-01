package news.molo.feature.auth.login.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.convelop.forge.navigation.NavigationEvent
import news.molo.core.designsystem.component.atom.ProgressIndicator
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.navigation.Screen
import news.molo.feature.auth.login.presentation.LoginState
import news.molo.feature.auth.login.presentation.LoginViewModel
import news.molo.feature.auth.login.ui.page.LoginPage

@Composable
fun LoginBottomSheet(
    viewModel: LoginViewModel,
    onDismiss: () -> Unit,
    onNavigationAction: (NavigationEvent) -> Unit

) {
    val state by viewModel.collectStateFlow().collectAsState()

    UIEventHandler(
        uiEvent = viewModel.events,
        onEventHandled = { event ->
            when (event) {
                UIEvent.NavigateBack -> onDismiss()
                is UIEvent.SnackBar -> {}
                else -> {}
            }
        }
    )

    LoginBottomSheetImpl(
        onDismiss = onDismiss,
        state = state,
        executeIntent = viewModel::executeIntent,
        onNavigationAction = onNavigationAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginBottomSheetImpl(
    onDismiss: () -> Unit,
    state: LoginState,
    executeIntent: (LoginState.Intent) -> Unit,
    onNavigationAction: (NavigationEvent) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
    ) {
        when (state) {
            is LoginState.Default -> LoginPage(
                mail = state.email,
                password = state.password,
                showError = !state.errorMessage.isNullOrBlank(),
                onMailChange = { executeIntent(LoginState.Default.Intent.SetEmail(it)) },
                onPasswordChange = { executeIntent(LoginState.Default.Intent.SetPassword(it)) },
                onClickRegister = { onNavigationAction(NavigationEvent.NavigateTo(Screen.Registration)) },
                onClickResetPassword = { onNavigationAction(NavigationEvent.NavigateTo(Screen.RequestPasswordReset)) },
                onClickLogin = { executeIntent(LoginState.Default.Intent.Login) }
            )

            is LoginState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { ProgressIndicator() }
        }
    }
}