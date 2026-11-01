package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun HeadlineTextField(
    modifier: Modifier = Modifier,
    headline: String,
    placeholder: String,
    onHeadlineChange: (String) -> Unit
) {
    BasicTextField(
        value = headline,
        onValueChange = onHeadlineChange,
        modifier = Modifier
            .then(modifier),
        textStyle = MaterialTheme.typography.headlineLarge.copy(
            color = Color(0xFFFFC092),
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 40.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (headline.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color(0xFFFFC092),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    innerTextField()
                }
                HorizontalDivider(modifier = Modifier.padding(top = Spacing.Small))
            }
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        HeadlineTextField(
            headline = "Headline",
            placeholder = "Placeholder"
        ) { }
    }
}