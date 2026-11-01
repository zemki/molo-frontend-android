package news.molo.feature.search.result.ui

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.organism.PagerWithIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.search.result.presentation.SearchResultType

@Composable
internal fun SearchResultPage(
    query: String,
    tabs: List<SearchResultType>,
    selectedTab: SearchResultType,
    onQueryChange: (String) -> Unit,
    onTabSelectionChange: (SearchResultType) -> Unit,
    tabContent: @Composable (SearchResultType) -> Unit,
) {
    PagerWithIndicator(
        tabs = tabs,
        selectedTab = selectedTab,
        onTabSelected = onTabSelectionChange,
        tabText = { selectedTab ->
            when (selectedTab) {
                SearchResultType.Article -> Text(text = stringResource(R.string.search_headline_article))
                SearchResultType.Events -> Text(text = stringResource(R.string.search_headline_event))
            }
        },
        tabIcon = { selectedTab ->
            when (selectedTab) {
                SearchResultType.Article -> Image(
                    painter = painterResource(R.drawable.icn_article_types),
                    contentDescription = null
                )

                SearchResultType.Events -> Icon(
                    painter = painterResource(R.drawable.icn_events),
                    contentDescription = null
                )
            }
        },
        contentForPage = tabContent
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        SearchResultPage(
            query = "abc",
            tabs = SearchResultType.entries,
            selectedTab = SearchResultType.Article,
            onQueryChange = {},
            onTabSelectionChange = { },
            tabContent = { }
        )
    }
}
