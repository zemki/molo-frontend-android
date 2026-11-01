package news.molo.feature.organization.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import news.molo.core.designsystem.component.molecule.PostDeletionAlert
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.feature.article.FeedArticleItem

@Composable
fun OrganizationArticlePage(
    pagingDataFlow: Flow<PagingData<Article>>,
    listState: LazyListState = rememberLazyListState(),
    areArticleDeletable: Boolean,
    onArticleClick: (Int) -> Unit,
    onDeleteClick: ((Int) -> Unit)? = null,
) {

    val pagingData = pagingDataFlow.collectAsLazyPagingItems()
    val isEmpty = pagingData.loadState.refresh is LoadState.NotLoading && pagingData.itemCount == 0
    var articleIdToDelete by rememberSaveable { mutableStateOf<Int?>(null) }

    if (articleIdToDelete != null) {
        PostDeletionAlert(
            onConfirm = {
                onDeleteClick?.invoke(articleIdToDelete ?: 0)
                articleIdToDelete = null
            },
            onDismiss = { articleIdToDelete = null }
        )
    }

    when (isEmpty) {
        true -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.article_no_results_placeholder_text))
        }

        false -> LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(count = pagingData.itemCount) { index ->
                pagingData[index]?.let { article ->
                    FeedArticleItem(
                        article = article,
                        onArticleClick = { onArticleClick(it.id) },
                        onPublisherClick = {},
                        onClickDelete = if (areArticleDeletable) {
                            {
                                articleIdToDelete = article.id
                            }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        OrganizationArticlePage(
            pagingDataFlow = flow { PagingData.from(listOf(Article.mock)) },
            onArticleClick = {},
            areArticleDeletable = false
        )
    }
}