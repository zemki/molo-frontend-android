package news.molo.core.designsystem.component.molecule

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun DestructiveTextButton(modifier: Modifier = Modifier, title: String, onClick: () -> Unit) {
    TextButton(
        modifier = Modifier.then(modifier),
        onClick = onClick
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DestructiveTextButton(
            title = "Destructive Action",
            onClick = {}
        )
    }
}