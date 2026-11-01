package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun CircularIcon(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {

    val backgroundColor = Color.White

    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(100)
            )
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(12.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        CircularIcon(
            icon = {
                Icon(Icons.Default.Settings, contentDescription = null)
            }
        )
    }
}