package news.molo.feature.tags.ui.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.organism.PagerWithIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.TabSelectionType

@Composable
internal fun TagSelectionPage(
    tabs: List<TabSelectionType>,
    selectedTab: TabSelectionType,
    onTabSelect: (TabSelectionType) -> Unit,
    tabContent: @Composable (TabSelectionType) -> Unit
) {
    PagerWithIndicator(
        tabs = tabs,
        selectedTab = selectedTab,
        onTabSelected = onTabSelect,
        tabText = { selectedTab ->
            when (selectedTab) {
                TabSelectionType.Article -> Text(text = stringResource(R.string.tag_selection_headline_article))
                TabSelectionType.Events -> Text(text = stringResource(R.string.tag_selection_headline_event))
            }
        },
        contentForPage = tabContent
    )
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        TagSelectionPage(
            tabs = TabSelectionType.entries,
            selectedTab = TabSelectionType.Events,
            onTabSelect = {},
            tabContent = {}
        )
    }
}