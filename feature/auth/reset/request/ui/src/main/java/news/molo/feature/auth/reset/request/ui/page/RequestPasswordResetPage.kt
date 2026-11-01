package news.molo.feature.auth.reset.request.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.molecule.ConfirmableDialog
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RequestPasswordResetPage(
    mail: String,
    emailRequested: Boolean,
    errorMessage: String?,
    requestingEnabled: Boolean,
    onErrorAlertConfirmClick: () -> Unit,
    onConfirmAlertConfirmClick: () -> Unit,
    onMailChange: (String) -> Unit,
    onClickRequest: () -> Unit
) {

    errorMessage?.takeUnless { it.isEmpty() }?.let { error ->
        ConfirmableDialog(
            title = stringResource(R.string.password_reset_error_alert_title),
            text = error,
            onConfirm = onErrorAlertConfirmClick,
            onDismiss = onErrorAlertConfirmClick
        )
    }

    if (emailRequested) {
        ConfirmableDialog(
            title = stringResource(R.string.password_reset_request_success_alert_title),
            text = stringResource(R.string.password_reset_request_success_alert_text),
            onConfirm = onConfirmAlertConfirmClick,
            onDismiss = onConfirmAlertConfirmClick
        )
    }

    PageTemplate {
        Text(
            modifier = Modifier.padding(vertical = Spacing.Medium),
            text = stringResource(R.string.password_reset_request_headline_text)
        )

        TextField(
            value = mail,
            placeholder = stringResource(R.string.password_reset_mail_hint),
            keyboardType = KeyboardType.Email,
            contentType = ContentType.EmailAddress,
            imeAction = ImeAction.Done,
            onValueChange = onMailChange
        )

        PrimaryButton(
            modifier = Modifier.padding(top = Spacing.Medium),
            onClick = onClickRequest,
            text = stringResource(R.string.password_reset_next_button_text),
            enabled = requestingEnabled
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        RequestPasswordResetPage(
            mail = "max@example.com",
            emailRequested = false,
            errorMessage = null,
            requestingEnabled = true,
            onErrorAlertConfirmClick = {},
            onConfirmAlertConfirmClick = {},
            onMailChange = {},
            onClickRequest = {}
        )
    }
}