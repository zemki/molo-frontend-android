package news.molo.core.designsystem.component.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun <T> ExpandableListView(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemKey: (T) -> Any,
    expandedItemKeys: Set<Any>,
    itemContent: @Composable ColumnScope.(item: T, isExpanded: Boolean) -> Unit,
    expandedContent: LazyListScope.(item: T) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        items.forEach { item ->
            val key = itemKey(item)
            val isExpanded = expandedItemKeys.contains(key)

            item(key = key) {
                Column {
                    itemContent(item, isExpanded)
                }
            }

            if (isExpanded) {
                expandedContent(item)
            }
        }
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    val items = listOf("Item #1", "Item #2", "item #3")
    ComponentPreviewTemplate {
        ExpandableListView(
            items = items,
            itemKey = { it },
            expandedItemKeys = setOf(items.first()),
            itemContent = { item, isSelected ->

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.Medium),
                        text = "Header: $item"
                    )
                }

            },
            expandedContent = {
                items(10) {
                    Text(text = "Item $it")
                }
            }
        )
    }
}