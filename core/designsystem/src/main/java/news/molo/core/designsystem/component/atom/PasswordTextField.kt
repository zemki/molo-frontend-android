package news.molo.core.designsystem.component.atom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    placeholder: String,
    contentType: ContentType = ContentType.Password,
    imeAction: ImeAction = ImeAction.Companion.Next,
    onPasswordChanged: (String) -> Unit,
    errorText: String? = null,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .then(modifier),
        value = password,
        placeholder = placeholder,
        errorText = errorText,
        onValueChange = onPasswordChanged,
        imeAction = imeAction,
        keyboardType = KeyboardType.Password,
        contentType = contentType,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        PasswordTextField(
            password = "password",
            placeholder = "Password",
            contentType = ContentType.Password,
            onPasswordChanged = {}
        )
    }
}