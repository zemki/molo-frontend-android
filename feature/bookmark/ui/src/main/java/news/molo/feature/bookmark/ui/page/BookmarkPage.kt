package news.molo.feature.bookmark.ui.page

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
import news.molo.feature.bookmark.presentation.BookmarkType
import news.molo.feature.bookmark.presentation.BookmarksState

@Composable
internal fun BookmarkPage(
    tabs: List<BookmarkType>,
    selectedTab: BookmarkType,
    onTabSelectionChange: (BookmarkType) -> Unit,
    tabContent: @Composable (BookmarkType) -> Unit,
) {
    PagerWithIndicator(
        tabs = tabs,
        selectedTab = selectedTab,
        onTabSelected = onTabSelectionChange,
        tabText = { selectedTab ->
            when (selectedTab) {
                BookmarkType.Article -> Text(text = stringResource(R.string.bookmarked_headline_article))
                BookmarkType.Events -> Text(text = stringResource(R.string.bookmarked_headline_event))
            }
        },
        tabIcon = { selectedTab ->
            when (selectedTab) {
                BookmarkType.Article -> Image(
                    painter = painterResource(R.drawable.icn_article_types),
                    contentDescription = null
                )

                BookmarkType.Events -> Icon(
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
    val state = BookmarksState()

    ComponentPreviewTemplate {
        BookmarkPage(
            tabs = BookmarkType.entries,
            selectedTab = BookmarkType.Article,
            onTabSelectionChange = {  },
            tabContent = {  }
        )
    }
}
