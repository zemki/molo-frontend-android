package news.molo.feature.auth.reset.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import news.molo.core.designsystem.component.atom.PasswordTextField
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.molecule.ConfirmableDialog
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.auth.reset.presentation.PasswordResetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasswordResetPage(
    password: String,
    repeatPassword: String,
    errorMessage: String?,
    emailRequested: Boolean,
    onChangePassword: (String) -> Unit,
    onChangeRepeatPassword: (String) -> Unit,
    onConfirmConfirmationClick: () -> Unit,
    onClickRequestReset: () -> Unit
) {

    if (emailRequested) {
        ConfirmableDialog(
            title = stringResource(R.string.password_reset_success_alert_title),
            text = stringResource(R.string.password_reset_success_alert_text),
            onConfirm = onConfirmConfirmationClick,
            onDismiss = onConfirmConfirmationClick
        )
    }

    PageTemplate(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        Text(
            modifier = Modifier.padding(vertical = Spacing.Medium),
            text = stringResource(R.string.password_reset_headline_text)
        )

        PasswordTextField(
            password = password,
            placeholder = stringResource(R.string.password_reset_password_hint),
            errorText = errorMessage,
            onPasswordChanged = onChangePassword
        )

        PasswordTextField(
            password = repeatPassword,
            placeholder = stringResource(R.string.password_reset_repeat_password_hint),
            imeAction = ImeAction.Done,
            errorText = errorMessage,
            onPasswordChanged = onChangeRepeatPassword
        )

        PrimaryButton(
            modifier = Modifier.padding(top = Spacing.Medium),
            onClick = onClickRequestReset,
            text = stringResource(R.string.password_reset_reset_button_text)
        )
    }
}

private class Provider : PreviewParameterProvider<PasswordResetState.Default> {
    override val values = sequenceOf(
        PasswordResetState.Default(),
        PasswordResetState.Default(password = "password", repeatPassword = "password123")
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: PasswordResetState.Default
) {

    ComponentPreviewTemplate {
        PasswordResetPage(
            password = "abc",
            repeatPassword = "abc",
            errorMessage = null,
            emailRequested = false,
            onChangePassword = { },
            onChangeRepeatPassword = { },
            onConfirmConfirmationClick = { },
            onClickRequestReset = { }
        )
    }
}