package news.molo.feature.bookmark.ui.page

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ListTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.feature.article.FeedArticleItem

@Composable
internal fun BookmarkedArticlesPage(
    articlePagingDataFlow: Flow<PagingData<Article>>,
    onArticleClick: (Int) -> Unit,
    onOrganizationClick: (Int) -> Unit,
) {

    val pagingData = articlePagingDataFlow.collectAsLazyPagingItems()

    PageTemplate {
        ListTemplate {
            items(count = pagingData.itemCount) { index ->
                val article = pagingData[index] ?: return@items
                FeedArticleItem(
                    article = article,
                    onArticleClick = { onArticleClick(it.id) },
                    onPublisherClick = onOrganizationClick
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        BookmarkedArticlesPage(
            articlePagingDataFlow = flowOf(PagingData.from(listOf(Article.mock, Article.mock))),
            onArticleClick = {},
            onOrganizationClick = { },
        )
    }
}