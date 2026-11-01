package news.molo.feature.area.ui.page

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import news.molo.core.designsystem.component.molecule.TextListItem
import news.molo.core.designsystem.component.organism.SearchableList
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Area

@Composable
internal fun AreaSelectionPage(
    query: String,
    areas: List<Area>,
    onQueryChange: (String) -> Unit,
    onAreaClick: (Area) -> Unit
) {

    PageTemplateWithPaddingSlot { contentPadding ->
        SearchableList(
            contentPadding = contentPadding,
            query = query,
            onQueryChanged = onQueryChange,
            content = {
                items(areas, key = { it.id }) { area ->
                    TextListItem(text = area.name, onClick = { onAreaClick(area) })
                }
            }
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        AreaSelectionPage(
            query = "",
            areas = List(4) { index -> Area.mock.copy(id = index + 1) },
            onQueryChange = {},
            onAreaClick = {}
        )
    }
}