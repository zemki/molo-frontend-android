package news.molo.core.designsystem.component.organism

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun CollapsingHeaderPage(
    modifier: Modifier = Modifier,
    header: @Composable BoxScope.() -> Unit,
    collapseProgress: Float,
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    var measuredHeaderHeightDp by remember { mutableStateOf<Dp?>(null) }

    val animatedHeight = measuredHeaderHeightDp?.let {
        animateDpAsState(
            targetValue = lerp(it, 0.dp, collapseProgress.coerceIn(0f, 1f)),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "HeaderCollapse"
        ).value
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (animatedHeight != null) Modifier.height(animatedHeight)
                    else Modifier.wrapContentHeight()
                )
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    if (measuredHeaderHeightDp == null) {
                        with(density) {
                            measuredHeaderHeightDp = coordinates.size.height.toDp() + Spacing.Medium
                        }
                    }
                }
        ) {
            header()
        }

        content()
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        CollapsingHeaderPage(
            header = {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.Medium)
                ) {
                    Column(modifier = Modifier.padding(Spacing.Medium)) {
                        Text(text = "Thats the header")
                        Text(text = "[collapseProgress = 0] == Fully visible")
                        Text(text = "[collapseProgress = 0.5] == Half Visible")
                        Text(text = "[collapseProgress = 1] == Gone")
                    }
                }
            },
            collapseProgress = 0f,
            content = {
                LazyColumn {
                    items(10) {
                        Text(text = "Item #$it")
                    }
                }
            }
        )
    }
}