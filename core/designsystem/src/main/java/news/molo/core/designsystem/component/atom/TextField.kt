package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    imeAction: ImeAction = ImeAction.Next,
    contentType: ContentType? = null,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentType?.let { this.contentType = it }
            }
            .then(modifier),
        value = value,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.MoloOrange,
            cursorColor = Color.MoloOrange,
            focusedLabelColor = Color.MoloOrange
        ),
        isError = !errorText.isNullOrBlank(),
        supportingText = if (errorText.isNullOrBlank()) {
            null
        } else {
            { Text(text = errorText) }
        },
        placeholder = {
            Text(text = placeholder)
        },
        label = {
            Text(text = placeholder)
        },
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        enabled = enabled,
        keyboardOptions = KeyboardOptions.Companion.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        onValueChange = onValueChange,
        singleLine = isSingleLine,
        minLines = minLines,
        maxLines = maxLines
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        TextField(
            value = "",
            placeholder = "Test",
            onValueChange = {}
        )
    }
}