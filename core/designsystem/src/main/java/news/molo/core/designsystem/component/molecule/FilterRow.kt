package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun <T> FilterRow(
    modifier: Modifier = Modifier,
    items: List<T>,
    centerItems: Boolean = false,
    filterItem: @Composable RowScope.(item: T) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .then(modifier),
        horizontalArrangement = if (centerItems) {
            Arrangement.spacedBy(Spacing.Small, Alignment.CenterHorizontally)
        } else {
            Arrangement.spacedBy(Spacing.Small)
        }
    ) {
        items.forEach { item ->
            filterItem(item)
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        FilterRow(
            items = listOf("Filter #1", "Filter #2")
        ) {
            FilterItem(title = it, selected = true) { }
        }
    }
}