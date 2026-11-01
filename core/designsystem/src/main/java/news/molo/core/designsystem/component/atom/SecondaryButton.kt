package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.LocalIsDarkTheme
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    val size = ButtonDefaults.MediumContainerHeight

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(size)
            .then(modifier),
        contentPadding = ButtonDefaults.contentPaddingFor(size),
        onClick = onClick,
        shapes = ButtonDefaults.shapes(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = if (LocalIsDarkTheme.current) Color.White else Color.MoloOrange),
        border = ButtonDefaults.outlinedButtonBorder().copy(brush = SolidColor(Color.MoloOrange)),
        content = {
            Text(text = text, style = ButtonDefaults.textStyleFor(size))
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        SecondaryButton(
            text = "Example Button",
            onClick = {}
        )
    }
}