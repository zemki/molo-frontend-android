package news.molo.feature.info.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.SecondaryButton
import news.molo.core.designsystem.component.molecule.DestructiveTextButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
internal fun InfoActionButtons(
    isLoggedIn: Boolean,
    onRequestLogin: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    when (isLoggedIn) {
        true -> LoggedInActionButtons(onLogout = onLogout, onDeleteAccount = onDeleteAccount)
        false -> LoggedOutActionButtons(onRequestLogin = onRequestLogin)
    }
}

@Composable
private fun LoggedInActionButtons(
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SecondaryButton(
            onClick = onLogout,
            text = stringResource(R.string.profile_logout_button_text),
            modifier = Modifier.padding(
                bottom = Spacing.Small
            )
        )
        DestructiveTextButton(
            title = stringResource(R.string.profile_delete_account_button_text),
            onClick = onDeleteAccount
        )
    }
}

@Composable
private fun LoggedOutActionButtons(onRequestLogin: () -> Unit) {
    PrimaryButton(
        text = stringResource(R.string.profile_login_button_text),
        onClick = onRequestLogin
    )
}

private class Provider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) isLoggedIn: Boolean
) {
    ComponentPreviewTemplate {
        InfoActionButtons(
            isLoggedIn = isLoggedIn,
            onRequestLogin = {},
            onLogout = {},
            onDeleteAccount = {}
        )
    }
}