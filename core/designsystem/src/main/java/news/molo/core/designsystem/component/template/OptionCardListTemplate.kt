package news.molo.core.designsystem.component.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.theme.CardBackground
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.preview.ComponentPreview

@Composable
fun <T> OptionCardListTemplate(
    modifier: Modifier = Modifier,
    items: List<T>,
    cardContent: @Composable (item: T) -> Unit,
) {

    val containerColor = when (MaterialTheme.isDarkTheme) {
        true -> MaterialTheme.colorScheme.surfaceContainerLow
        false -> Color.CardBackground
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.ExtraExtraSmall)
    ) {
        items.forEachIndexed { index, item ->
            val shape = when (index) {
                0 -> MaterialTheme.shapes.medium.copy(
                    bottomEnd = CornerSize(CornerRadius.ExtraSmall),
                    bottomStart = CornerSize(CornerRadius.ExtraSmall)
                )

                items.lastIndex -> MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(CornerRadius.ExtraSmall),
                    topEnd = CornerSize(CornerRadius.ExtraSmall)
                )

                else -> RoundedCornerShape(CornerRadius.ExtraSmall)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = shape,
                colors = CardDefaults.cardColors(containerColor = containerColor)

            ) {
                cardContent(item)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        OptionCardListTemplate(
            items = listOf("#1 Example", "#2 Example", "#3 Example")
        ) {
            Column(modifier = Modifier.padding(Spacing.Medium)) {
                Text(text = it)
            }
        }
    }
}