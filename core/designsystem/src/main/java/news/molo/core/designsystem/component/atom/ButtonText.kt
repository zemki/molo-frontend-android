package news.molo.core.designsystem.component.atom

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun ButtonText(modifier: Modifier = Modifier, text: String) {
    Text(modifier = Modifier.then(modifier), text = text)
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ButtonText(
            text = "Example Text"
        )
    }
}