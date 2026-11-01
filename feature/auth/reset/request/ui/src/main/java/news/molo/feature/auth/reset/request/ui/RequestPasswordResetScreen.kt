package news.molo.feature.auth.reset.request.ui

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
import news.molo.feature.auth.reset.request.presentation.RequestPasswordResetState
import news.molo.feature.auth.reset.request.presentation.RequestPasswordResetViewModel
import news.molo.feature.auth.reset.request.ui.page.RequestPasswordResetPage

@Composable
fun RequestPasswordResetScreen(
    viewModel: RequestPasswordResetViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    RequestPasswordResetScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RequestPasswordResetScreenImpl(
    state: RequestPasswordResetState,
    executeIntent: (RequestPasswordResetState.Intent) -> Unit
) {

    ScreenScaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.title_password_reset),
                onNavigateBackClick = { executeIntent(RequestPasswordResetState.Intent.NavigateUp) }
            )
        }
    ) {
        when (state) {
            is RequestPasswordResetState.Default -> RequestPasswordResetPage(
                mail = state.email,
                emailRequested = state.emailRequested,
                errorMessage = state.errorMessage,
                requestingEnabled = state.requestingEnabled,
                onErrorAlertConfirmClick = { executeIntent(RequestPasswordResetState.Default.Intent.Retry) },
                onConfirmAlertConfirmClick = { executeIntent(RequestPasswordResetState.Intent.NavigateUp) },
                onMailChange = { executeIntent(RequestPasswordResetState.Default.Intent.SetEmail(it)) },
                onClickRequest = { executeIntent(RequestPasswordResetState.Default.Intent.RequestReset) }
            )

            RequestPasswordResetState.Loading -> CoveringProgressIndicator()
        }
    }
}

private class Provider : PreviewParameterProvider<RequestPasswordResetState.Default> {
    override val values = sequenceOf(
        RequestPasswordResetState.Default(),
        RequestPasswordResetState.Default(email = "info@example.com")
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: RequestPasswordResetState.Default
) {
    ComponentPreviewTemplate {
        RequestPasswordResetScreenImpl(
            state = state,
            executeIntent = {}
        )
    }
}