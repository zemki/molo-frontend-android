package news.molo.feature.posting.article.creation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.organism.ChipFlowRow
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory

@Composable
internal fun ArticleConfigurationPage(
    link: String,
    tagCategories: List<TagCategory>,
    onLinkChange: (String) -> Unit,
    onSelectTag: (Tag) -> Unit,
    onSelectCategory: (TagCategory) -> Unit
) {

    PageTemplateWithPaddingSlot { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {

            item(key = "fields") {
                TextField(
                    value = link,
                    placeholder = stringResource(R.string.article_posting_link_hint),
                    onValueChange = onLinkChange
                )
            }

            item(key = "tag_description") {
                TagRowHeader()
            }

            items(tagCategories, key = { it.id }) { category ->
                TagFlowRow(
                    category = category,
                    onSelectTag = onSelectTag,
                    onSelectCategory = onSelectCategory
                )
            }
        }
    }
}

@Composable
private fun TagFlowRow(
    category: TagCategory,
    onSelectTag: (Tag) -> Unit,
    onSelectCategory: (TagCategory) -> Unit
) {
    ChipFlowRow(
        title = category.name,
        items = category.tags,
        chip = { tag ->
            FilterItem(
                title = tag.name,
                selected = tag.isSelected,
                onSelectedChange = { onSelectTag(tag) }
            )
        },
        onToggleAllSelected = { onSelectCategory(category) }
    )
}

@Composable
private fun TagRowHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.Small)
        ) {
            Icon(Icons.Default.Tag, contentDescription = null, tint = Color.MoloOrange)
            Text(text = stringResource(R.string.article_posting_tags_headline), fontWeight = FontWeight.Medium, fontSize = 15.sp)
        }

        Text(text = stringResource(R.string.article_posting_tags_description))
    }
}