package news.molo.feature.tags.ui.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.organism.ChipFlowRow
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.designsystem.theme.Spacing
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory

@Composable
internal fun TagCategoryFlowRowPage(
    tagCategories: List<TagCategory>,
    onTagClick: (Tag) -> Unit,
    onToggleAllInCategoryClick: (TagCategory) -> Unit
) {

    PageTemplateWithPaddingSlot { _ ->
        if (tagCategories.isEmpty()) {
            CoveringProgressIndicator()
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Spacing.Medium,
                end = Spacing.Medium,
                top = Spacing.Medium,
                bottom = Spacing.Medium + 80.dp
            )
        ) {
            items(tagCategories, key = { it.id }) { category ->
                TagCategoryFlowRow(
                    category = category,
                    onTagClick = onTagClick,
                    onToggleAllInCategoryClick = onToggleAllInCategoryClick
                )
            }
        }
    }
}

@Composable
private fun TagCategoryFlowRow(
    category: TagCategory,
    onTagClick: (Tag) -> Unit,
    onToggleAllInCategoryClick: (TagCategory) -> Unit
) {
    ChipFlowRow(
        title = category.name,
        items = category.tags,
        chip = { tag ->
            FilterItem(
                title = tag.name,
                selected = tag.isSelected,
                onSelectedChange = { onTagClick(tag) }
            )
        },
        onToggleAllSelected = { onToggleAllInCategoryClick(category) }
    )
}

@Preview
@Composable
private fun PreviewTagCategoryFlowRowPage() {
    ComponentPreviewTemplate {
        TagCategoryFlowRowPage(
            tagCategories = listOf(
                TagCategory.mock,
                TagCategory.mock.copy(id = 2, name = "Another Category")
            ),
            onTagClick = {},
            onToggleAllInCategoryClick = {}
        )
    }
}
