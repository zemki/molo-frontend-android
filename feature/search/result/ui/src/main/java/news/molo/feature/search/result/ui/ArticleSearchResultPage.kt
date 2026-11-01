package news.molo.feature.search.result.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.organism.RefreshableList
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.util.paging.rememberManualRefreshState
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.feature.article.FeedArticleItem

@Composable
internal fun ArticleSearchResultPage(
    articlePagingDataFlow: Flow<PagingData<Article>>,
    onArticleClick: (Int) -> Unit,
    onOrganizationClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {

    val pagingData = articlePagingDataFlow.collectAsLazyPagingItems()
    val refreshState = rememberManualRefreshState(pagingData.loadState.refresh)
    var hasInitialLoadStarted by remember { mutableStateOf(false) }

    LaunchedEffect(pagingData.loadState.refresh) {
        if (pagingData.loadState.refresh is LoadState.Loading) {
            hasInitialLoadStarted = true
        }
    }
    val isListEmpty = hasInitialLoadStarted &&
            pagingData.loadState.refresh is LoadState.NotLoading &&
            pagingData.itemCount == 0
    PageTemplate {
        RefreshableList(
            isRefreshing = refreshState.isRefreshing,
            showLoading = refreshState.showLoading,
            isEmpty = isListEmpty,
            onRefresh = {
                refreshState.start()
                onRefresh()
            },
            emptyContent = {
                NoSearchResultsScreen()
            },
            content = {
                items(count = pagingData.itemCount) { index ->
                    val article = pagingData[index] ?: return@items
                    FeedArticleItem(
                        article = article,
                        onArticleClick = {
                            onArticleClick(article.id)
                        },
                        onPublisherClick = {
                            onOrganizationClick(it)
                        }
                    )
                }
            }
        )
    }
}


@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ArticleSearchResultPage(
            articlePagingDataFlow = flowOf(PagingData.from(listOf(Article.mock, Article.mock))),
            onArticleClick = {},
            onOrganizationClick = { },
            onRefresh = {}
        )
    }
}