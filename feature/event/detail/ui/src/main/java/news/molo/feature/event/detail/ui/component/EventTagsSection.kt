package news.molo.feature.event.detail.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Tag

@Composable
internal fun EventTagsSection(tags: List<Tag>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
    ) {
        tags.forEach {
            FilterItem(
                title = it.name,
                selected = true,
                onSelectedChange = {}
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        EventTagsSection(
            tags = listOf(Tag.mock, Tag.mock)
        )
    }
}