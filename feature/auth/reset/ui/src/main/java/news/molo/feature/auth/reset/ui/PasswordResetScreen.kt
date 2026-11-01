package news.molo.feature.auth.reset.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.auth.reset.presentation.PasswordResetState
import news.molo.feature.auth.reset.presentation.PasswordResetViewModel
import news.molo.feature.auth.reset.ui.page.PasswordResetPage

@Composable
fun PasswordResetScreen(
    viewModel: PasswordResetViewModel
) {

    val state by viewModel.collectStateFlow().collectAsState()

    PasswordResetScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordResetScreenImpl(
    state: PasswordResetState,
    executeIntent: (PasswordResetState.Intent) -> Unit
) {

    ScreenScaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.title_password_reset),
                onNavigateBackClick = { executeIntent(PasswordResetState.Intent.NavigateUp) }
            )
        }
    ) {
        when (state) {
            is PasswordResetState.Default -> PasswordResetPage(
                password = state.password,
                repeatPassword = state.repeatPassword,
                errorMessage = state.errorMessage,
                emailRequested = state.emailRequested,
                onChangePassword = { executeIntent(PasswordResetState.Default.Intent.SetPassword(it)) },
                onChangeRepeatPassword = { executeIntent(PasswordResetState.Default.Intent.SetRepeatPassword(it)) },
                onConfirmConfirmationClick = { executeIntent(PasswordResetState.Default.Intent.Retry) },
                onClickRequestReset = { executeIntent(PasswordResetState.Default.Intent.RequestReset) }
            )

            PasswordResetState.Loading -> CoveringProgressIndicator()
        }
    }
}

private class Provider : PreviewParameterProvider<PasswordResetState.Default> {
    override val values = sequenceOf(
        PasswordResetState.Default(),
        PasswordResetState.Default(password = "password", "password123")
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: PasswordResetState.Default
) {
    ComponentPreviewTemplate {
        PasswordResetScreenImpl(
            state = state,
            executeIntent = {}
        )
    }
}