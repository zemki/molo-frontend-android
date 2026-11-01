package news.molo.core.designsystem.component.molecule

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CardBackground
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
fun ExpandableOrganizationSummaryHeader(
    title: String,
    @DrawableRes
    iconRes: Int,
    onClick: () -> Unit,
    isExpanded: Boolean,
    content: @Composable (() -> Unit) = {},
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "ExpandRotation"
    )

    val containerColor = when (MaterialTheme.isDarkTheme) {
        true -> MaterialTheme.colorScheme.surfaceContainerLow
        false -> Color.CardBackground
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            onClick = onClick,
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.Medium),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(Spacing.Medium))
                Text(modifier = Modifier.weight(1f), text = title)
                Icon(
                    painter = painterResource(R.drawable.icn_expand_indicator),
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotationAngle
                        },
                )
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Medium, vertical = Spacing.Small)
            ) {
                content()
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ExpandableOrganizationSummaryHeader(
            title = "Medien",
            iconRes = R.drawable.icn_community,
            onClick = {},
            isExpanded = true,
            content = {

            }
        )
    }
}