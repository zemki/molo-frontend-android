package news.molo.feature.auth.login.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import news.molo.core.designsystem.component.atom.PasswordTextField
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.SecondaryButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.auth.login.presentation.LoginState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginPage(
    mail: String,
    password: String,
    showError: Boolean,
    onMailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClickLogin: () -> Unit,
    onClickRegister: () -> Unit,
    onClickResetPassword: () -> Unit,
) {

    PageTemplate(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        TextHeader()

        TextField(
            modifier = Modifier.padding(bottom = Spacing.Small),
            value = mail,
            placeholder = stringResource(R.string.login_mail_hint),
            keyboardType = KeyboardType.Email,
            errorText = if (showError) stringResource(R.string.login_invalid_mail_or_password_hint) else null,
            contentType = ContentType.EmailAddress,
            onValueChange = onMailChange,
        )

        PasswordTextField(
            modifier = Modifier.padding(bottom = Spacing.Medium),
            password = password,
            placeholder = stringResource(R.string.login_password_hint),
            errorText = if (showError) stringResource(R.string.login_invalid_mail_or_password_hint) else null,
            imeAction = ImeAction.Done,
            onPasswordChanged = onPasswordChange
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        PrimaryButton(
            modifier = Modifier.padding(bottom = Spacing.Small),
            text = stringResource(R.string.login_button_text),
            onClick = onClickLogin
        )

        SecondaryButton(
            text = stringResource(R.string.login_registration_button_text),
            onClick = onClickRegister
        )

        Spacer(modifier = Modifier.weight(1f))

        ForgetPasswordTextButton(onClick = onClickResetPassword)
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
            text = stringResource(R.string.login_header_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.login_header_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ColumnScope.ForgetPasswordTextButton(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.login_forgot_password_button_text),
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .padding(Spacing.Medium)
            .align(Alignment.CenterHorizontally)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}

private class Provider : PreviewParameterProvider<LoginState.Default> {
    override val values = sequenceOf(
        LoginState.Default(),
        LoginState.Default(
            email = "info@example.com",
            password = "password1234"
        )
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: LoginState.Default
) {

    ComponentPreviewTemplate {
        LoginPage(
            mail = "max@example.com",
            password = "abc",
            showError = true,
            onMailChange = {},
            onPasswordChange = {},
            onClickLogin = {},
            onClickRegister = {},
            onClickResetPassword = {}
        )
    }
}