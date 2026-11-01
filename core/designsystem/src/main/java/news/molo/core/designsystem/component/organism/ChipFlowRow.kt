package news.molo.core.designsystem.component.organism

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.molecule.SelectAllRow
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun <T> ChipFlowRow(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    chip: @Composable FlowRowScope.(item: T) -> Unit,
    onToggleAllSelected: () -> Unit
) {

    Column(modifier = Modifier.then(modifier)) {
        SelectAllRow(title = title, onToggleAllSelected = onToggleAllSelected)

        FlowRow(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
        ) {
            items.forEach {
                chip(it)
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        val filter = listOf("Filter #1", "Filter #2", "Filter #3")
        ChipFlowRow(
            title = "Title",
            items = filter,
            chip = {
                FilterItem(
                    title = it,
                    selected = true,
                    onSelectedChange = {}
                )
            },
            onToggleAllSelected = {}
        )
    }
}