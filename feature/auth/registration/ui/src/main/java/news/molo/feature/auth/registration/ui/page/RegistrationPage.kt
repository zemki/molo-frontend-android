package news.molo.feature.auth.registration.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import news.molo.core.designsystem.component.atom.PasswordTextField
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.molecule.ConfirmableDialog
import news.molo.core.designsystem.component.molecule.LabeledCheckbox
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegistrationPage(
    mail: String,
    password: String,
    firstName: String,
    lastName: String,
    displayName: String,
    agbAccepted: Boolean,
    errorMessage: String?,
    enableRegistration: Boolean,
    onConfirmErrorDialogClick: () -> Unit,
    onMailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onAgbAcceptedChange: (Boolean) -> Unit,
    onClickRegister: () -> Unit
) {

    errorMessage?.takeUnless { it.isEmpty() }?.let { error ->
        ConfirmableDialog(
            title = stringResource(R.string.registration_error_alert_title),
            text = error,
            onConfirm = onConfirmErrorDialogClick,
            onDismiss = onConfirmErrorDialogClick
        )
    }

    PageTemplate(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {

        TextHeader()

        Spacer(modifier = Modifier.height(Spacing.Medium))

        TextField(
            value = mail,
            placeholder = stringResource(R.string.registration_mail_hint),
            keyboardType = KeyboardType.Email,
            onValueChange = onMailChange,
            contentType = ContentType.NewUsername
        )

        PasswordTextField(
            password = password,
            placeholder = stringResource(R.string.registration_password_hint),
            onPasswordChanged = onPasswordChange,
            contentType = ContentType.NewPassword
        )

        TextField(
            value = firstName,
            placeholder = stringResource(R.string.profile_first_name_hint),
            onValueChange = onFirstNameChange,
            contentType = ContentType.PersonFirstName,
        )

        TextField(
            value = lastName,
            placeholder = stringResource(R.string.profile_last_name_hint),
            onValueChange = onLastNameChange,
            contentType = ContentType.PersonLastName,
        )

        TextField(
            value = displayName,
            placeholder = stringResource(R.string.registration_display_name_hint),
            onValueChange = onDisplayNameChange,
            imeAction = ImeAction.Done
        )

        LabeledCheckbox(
            checked = agbAccepted,
            onCheckedChange = onAgbAcceptedChange,
            text = buildAgbText()
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickRegister,
            text = stringResource(R.string.registration_button_text),
            enabled = enableRegistration
        )
    }
}

@Composable
private fun TextHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = Spacing.Small),
            text = stringResource(R.string.registration_header_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.registration_header_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}


@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        RegistrationPage(
            mail = "max@example.com",
            password = "abc",
            firstName = "Max",
            lastName = "Mustermann",
            displayName = "max123",
            agbAccepted = true,
            errorMessage = null,
            enableRegistration = true,
            onConfirmErrorDialogClick = { },
            onMailChange = { },
            onPasswordChange = { },
            onFirstNameChange = { },
            onLastNameChange = { },
            onDisplayNameChange = { },
            onAgbAcceptedChange = { },
            onClickRegister = { }
        )
    }
}