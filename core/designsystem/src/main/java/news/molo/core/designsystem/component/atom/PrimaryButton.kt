package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    val size = ButtonDefaults.MediumContainerHeight

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(size)
            .then(modifier),
        contentPadding = ButtonDefaults.contentPaddingFor(size),
        onClick = onClick,
        shapes = ButtonDefaults.shapes(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.MoloOrange, contentColor = Color.White),
        content = {
            Text(text = text, style = ButtonDefaults.textStyleFor(size))
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        PrimaryButton(
            text = "Example Button",
            onClick = {}
        )
    }
}