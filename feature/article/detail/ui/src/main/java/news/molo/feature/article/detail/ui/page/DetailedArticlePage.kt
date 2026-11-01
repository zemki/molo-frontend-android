package news.molo.feature.article.detail.ui.page

import androidx.compose.runtime.Composable
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.core.util.wrapper.WebViewScreen
import news.molo.domain.model.Article
import news.molo.feature.article.detail.presentation.DetailedArticleState

@Composable
internal fun DetailedArticlePage(
    state: DetailedArticleState.Loaded
) {
    WebViewScreen(state.article.link)
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DetailedArticlePage(
            state = DetailedArticleState.Loaded(Article.mock)
        )
    }
}