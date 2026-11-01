package news.molo.feature.feed.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.organism.RefreshableList
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.util.paging.rememberManualRefreshState
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.feature.article.FeedArticleItem
import news.molo.feature.feed.presentation.ArticleFilter
import news.molo.feature.feed.ui.component.ArticleFilterRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedPage(
    filter: List<ArticleFilter>,
    selectedFilter: ArticleFilter?,
    articlePagingDataFlow: Flow<PagingData<Article>>,
    onArticleClick: (Int) -> Unit,
    onOrganizationClick: (Int) -> Unit,
    onFilterSelectionChange: (ArticleFilter) -> Unit,
    onRefresh: () -> Unit
) {

    val pagingData = articlePagingDataFlow.collectAsLazyPagingItems()
    val refreshState = rememberManualRefreshState(pagingData.loadState.refresh)
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    val isScrollingDown by remember {
        derivedStateOf {
            listState.lastScrolledBackward || !listState.isScrollInProgress
        }
    }

    var previousIndex by remember { mutableIntStateOf(0) }
    var previousScrollOffset by remember { mutableIntStateOf(0) }

    val isScrollingUp = remember(listState) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex != previousIndex) {
                val up = listState.firstVisibleItemIndex < previousIndex
                previousIndex = listState.firstVisibleItemIndex
                up
            } else {
                val up = listState.firstVisibleItemScrollOffset < previousScrollOffset
                previousScrollOffset = listState.firstVisibleItemScrollOffset
                up
            }
        }
    }

    val showFilter by remember {
        derivedStateOf {
            isScrollingUp.value || !listState.isScrollInProgress
        }
    }

    PageTemplateWithPaddingSlot { innerPadding ->
        AnimatedVisibility(
            visible = showFilter,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            ArticleFilterRow(
                filter = filter,
                selectedFilter = selectedFilter,
                onSelectedChange = onFilterSelectionChange
            )
        }

        RefreshableList(
            state = listState,
            contentPadding = innerPadding,
            isRefreshing = refreshState.isRefreshing,
            showLoading = refreshState.showLoading,
            onRefresh = {
                refreshState.start()
                onRefresh()
            },
            content = {
                items(count = pagingData.itemCount) { index ->
                    val article = pagingData[index] ?: return@items
                    FeedArticleItem(
                        article = article,
                        onArticleClick = {
                            focusManager.clearFocus()
                            onArticleClick(article.id)
                        },
                        onPublisherClick = {
                            focusManager.clearFocus()
                            onOrganizationClick(it)
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun LazyListState.isScrollingDown(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex < firstVisibleItemIndex
            } else {
                previousScrollOffset < firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        FeedPage(
            filter = ArticleFilter.all(),
            selectedFilter = null,
            articlePagingDataFlow = flowOf(),
            onArticleClick = {},
            onOrganizationClick = {},
            onFilterSelectionChange = {},
            onRefresh = { },
        )
    }
}