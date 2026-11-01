package news.molo.core.designsystem.component.atom

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
fun SmallPrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {

    Button(
        modifier = Modifier
            .then(modifier),
        onClick = onClick,
        shapes = ButtonDefaults.shapes(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.MoloOrange,
            contentColor = Color.White
        ),
        content = {
            Text(text = text)
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        SmallPrimaryButton(
            text = "Example Button",
            onClick = {}
        )
    }
}