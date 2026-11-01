package news.molo.feature.article.detail.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.flow.SharedFlow
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.util.helper.shareArticle
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.feature.article.detail.presentation.DetailedArticleState
import news.molo.feature.article.detail.presentation.DetailedArticleViewModel
import news.molo.feature.article.detail.ui.component.DetailedArticleTopAppBar
import news.molo.feature.article.detail.ui.page.DetailedArticlePage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedArticleScreen(
    viewModel: DetailedArticleViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    DetailedArticleScreenImpl(
        state = state,
        uiEvents = viewModel.events,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailedArticleScreenImpl(
    state: DetailedArticleState,
    uiEvents: SharedFlow<UIEvent>,
    executeIntent: (DetailedArticleState.Intent) -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val article = (state as? DetailedArticleState.Loaded)?.article

    val onShare: (Article) -> Unit = { article ->
        context.shareArticle(
            title = article.title,
            link = article.link
        )
    }

    UIEventHandler(
        uiEvent = uiEvents,
        snackBarHostState = snackBarHostState,
    )

    when (state) {
        is DetailedArticleState.Loaded -> ScreenScaffold(
            topBar = {
                DetailedArticleTopAppBar(
                    article = article,
                    onNavigateBackClicked = { executeIntent(DetailedArticleState.Intent.NavigateUp) },
                    onBookmarkedClicked = {
                        executeIntent(DetailedArticleState.Loaded.Intent.ToggleBookmark)
                    },
                    onShareClicked = onShare,
                    onReportClicked = { executeIntent(DetailedArticleState.Loaded.Intent.Report) }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        ) {
            DetailedArticlePage(state)
        }

        DetailedArticleState.Loading -> CoveringProgressIndicator()
    }
}

private class Provider : PreviewParameterProvider<DetailedArticleState> {
    override val values = sequenceOf(
        DetailedArticleState.Loaded(Article.mock),
        DetailedArticleState.Loading
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: DetailedArticleState
) {
    ComponentPreviewTemplate {
        DetailedArticleScreenImpl(
            state = state,
            uiEvents = UIEvent.mock,
            executeIntent = {}
        )
    }
}
