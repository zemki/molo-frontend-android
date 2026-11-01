package news.molo.core.designsystem.component.atom

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = Modifier.then(modifier), color = Color.MoloOrange)
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ProgressIndicator()
    }
}