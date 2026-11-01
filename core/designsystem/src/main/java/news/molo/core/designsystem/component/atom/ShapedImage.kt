package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.util.preview.ComponentPreview


@Composable
fun ShapedImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    shape: Shape = RoundedCornerShape(CornerRadius.Medium)
) {
    ImageWithPlaceholder(
        modifier = Modifier
            .clip(shape)
            .then(modifier),
        imageUri = imageUrl
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ShapedImage(imageUrl = "")
    }
}