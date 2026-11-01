package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun InfoRow(
    icon: @Composable RowScope.() -> Unit,
    text: AnnotatedString
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(Spacing.Small))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        InfoRow(
            icon = {
                Icon(Icons.Default.Event, contentDescription = null)
            },
            text = AnnotatedString("Text")
        )
    }
}