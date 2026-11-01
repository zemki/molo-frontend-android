package news.molo.feature.article.detail.ui.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.molecule.DetailsTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article

@Composable
internal fun DetailedArticleTopAppBar(
    article: Article?,
    onNavigateBackClicked: () -> Unit,
    onShareClicked: (Article) -> Unit,
    onBookmarkedClicked: (Article) -> Unit,
    onReportClicked: (Article) -> Unit,
) {
    var moreMenuExpanded by remember { mutableStateOf(false) }

    DetailsTopAppBar(
        isBookmarked = article?.isBookmarked ?: false,
        onClickBack = onNavigateBackClicked,
        onClickBookmark = { article?.let { onBookmarkedClicked(it) } },
        onClickShare = { article?.let { onShareClicked(it) } },
        moreMenuExpanded = moreMenuExpanded,
        moreMenuContent = {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.article_report_action_text)) },
                onClick = {
                    moreMenuExpanded = false
                    article?.let { onReportClicked(it) }
                }
            )
        },
        onMoreMenuExpandedChange = {
            moreMenuExpanded = it
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DetailedArticleTopAppBar(
            article = Article.mock,
            onNavigateBackClicked = { },
            onShareClicked = { },
            onBookmarkedClicked = { },
            onReportClicked = { },
        )
    }
}