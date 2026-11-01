package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun TagItem(
    title: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(CornerRadius.ExtraSmall))
            .padding(horizontal = Spacing.Small, vertical = Spacing.ExtraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            CompositionLocalProvider(LocalContentColor provides Color.MoloOrange) {
                it()
            }
            Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        TagItem(
            title = "Versammlung",
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(Spacing.Medium),
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star Icon",
                    tint = Color.White
                )
            }
        )
    }
}
