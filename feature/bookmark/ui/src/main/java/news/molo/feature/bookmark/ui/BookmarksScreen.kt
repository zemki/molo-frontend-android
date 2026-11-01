package news.molo.feature.bookmark.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.domain.model.Event
import news.molo.feature.bookmark.presentation.BookmarkType
import news.molo.feature.bookmark.presentation.BookmarksState
import news.molo.feature.bookmark.presentation.BookmarksViewModel
import news.molo.feature.bookmark.ui.page.BookmarkPage
import news.molo.feature.bookmark.ui.page.BookmarkedArticlesPage
import news.molo.feature.bookmark.ui.page.BookmarkedEventsPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    BookmarksScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent,
        articlePagingData = viewModel.articlePagingData,
        eventPagingData = viewModel.eventPagingData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarksScreenImpl(
    state: BookmarksState,
    executeIntent: (BookmarksState.Intent) -> Unit,
    articlePagingData: Flow<PagingData<Article>>,
    eventPagingData: Flow<PagingData<Event>>
) {

    BookmarkPage(
        tabs = state.tabs,
        selectedTab = state.selectedTab,
        onTabSelectionChange = { executeIntent(BookmarksState.Intent.SetSelectedTab(it)) },
        tabContent = { selectedTab ->
            when (selectedTab) {
                BookmarkType.Article -> BookmarkedArticlesPage(
                    articlePagingDataFlow = articlePagingData,
                    onArticleClick = { executeIntent(BookmarksState.Intent.ToArticleDetails(it)) },
                    onOrganizationClick = { executeIntent(BookmarksState.Intent.ToOrganisation(it)) }
                )

                BookmarkType.Events -> BookmarkedEventsPage(
                    eventPagingDataFlow = eventPagingData,
                    onEventClick = { executeIntent(BookmarksState.Intent.ToEventDetail(it)) },
                    onOrganizationClick = { executeIntent(BookmarksState.Intent.ToOrganisation(it)) },
                )
            }
        },
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = BookmarksState()

    ComponentPreviewTemplate {
        BookmarksScreenImpl(
            state = state,
            executeIntent = {},
            articlePagingData = emptyFlow(),
            eventPagingData = emptyFlow(),
        )
    }
}