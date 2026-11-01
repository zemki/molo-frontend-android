package news.molo.feature.feed.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.feature.feed.presentation.FeedState
import news.molo.feature.feed.presentation.FeedViewModel
import news.molo.feature.feed.ui.page.FeedPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    FeedScreenImpl(
        articlePagingDataFlow = viewModel.articles,
        state = state,
        executeIntent = viewModel::executeIntent,
    )
}

@Composable
private fun FeedScreenImpl(
    articlePagingDataFlow: Flow<PagingData<Article>>,
    state: FeedState,
    executeIntent: (FeedState.Intent) -> Unit
) {
    FeedPage(
        filter = state.filter,
        selectedFilter = state.selectedFilter,
        articlePagingDataFlow = articlePagingDataFlow,
        onFilterSelectionChange = { executeIntent(FeedState.Intent.SelectFilter(it)) },
        onRefresh = { executeIntent(FeedState.Intent.Refresh) },
        onArticleClick = { executeIntent(FeedState.Intent.ToArticleDetails(it)) },
        onOrganizationClick = { executeIntent(FeedState.Intent.ToOrganisation(it)) },
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = FeedState()

    ComponentPreviewTemplate {
        FeedScreenImpl(
            articlePagingDataFlow = flowOf(),
            state = state,
            executeIntent = {}
        )
    }
}