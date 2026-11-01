package news.molo.feature.info.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.helper.rememberCustomTabsLauncher
import news.molo.core.util.helper.sendEmail
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.info.presentation.InfoState
import news.molo.feature.info.presentation.InfoViewModel
import news.molo.feature.info.presentation.util.InfoMenuItem
import news.molo.feature.info.ui.page.InfoPage

@Composable
fun InfoScreen(
    onRequestLogin: () -> Unit,
    viewModel: InfoViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    InfoScreenImpl(
        onRequestLogin = onRequestLogin,
        state = state,
        executeIntent = viewModel::executeIntent
    )
}

@Composable
private fun InfoScreenImpl(
    onRequestLogin: () -> Unit,
    state: InfoState,
    executeIntent: (InfoState.Intent) -> Unit
) {
    val context = LocalContext.current
    val launchInCustomTab = rememberCustomTabsLauncher()
    val isDarkTheme = MaterialTheme.isDarkTheme

    InfoPage(
        onRequestLogin = onRequestLogin,
        infoMenuItems = state.infoMenuItems,
        isLoggedIn = state.isLoggedIn,
        onClickMySources = { executeIntent(InfoState.Intent.ToOrganisationSummary) },
        onChangeTheme = { executeIntent(InfoState.Intent.SetColorSchema(it)) },
        onAccountDeletionRequest = { executeIntent(InfoState.Intent.DeleteAccount) },
        onClickLogout = { executeIntent(InfoState.Intent.Logout) },
        isDarkTheme = isDarkTheme,
        onOpenUrl = { launchInCustomTab(it) },
        onSendFeedback = {
            context.sendEmail(
                InfoMenuItem.FeedbackAndContact.MailContent.TO,
                InfoMenuItem.FeedbackAndContact.MailContent.SUBJECT,
                InfoMenuItem.FeedbackAndContact.MailContent.TEXT
            )
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = InfoState(isLoggedIn = true)

    ComponentPreviewTemplate {
        InfoScreenImpl(
            onRequestLogin = {},
            state = state,
            executeIntent = {}
        )
    }
}