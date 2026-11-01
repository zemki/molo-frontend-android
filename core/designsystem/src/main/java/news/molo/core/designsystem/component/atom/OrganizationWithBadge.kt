package news.molo.core.designsystem.component.atom

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
internal fun OrganizationWithBadge(
    logoUrl: String,
    @DrawableRes
    categoryIconRes: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(78.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        ElevatedCard(
            modifier = Modifier
                .size(64.dp),
            shape = RoundedCornerShape(CornerRadius.Medium),
            onClick = onClick,
            colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
        ) {
            ImageWithPlaceholder(
                modifier = Modifier.fillMaxSize(),
                imageUri = logoUrl,
                contentScale = ContentScale.Inside,
            )
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Center)
                .offset(x = 30.dp, y = -(25).dp)
        ) {
            Image(
                painter = painterResource(categoryIconRes),
                contentDescription = null,
                modifier = Modifier
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        OrganizationWithBadge(
            logoUrl = "",
            categoryIconRes = R.drawable.icn_community,
            onClick = {}
        )
    }
}