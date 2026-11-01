package news.molo.feature.article

import androidx.compose.runtime.Composable
import news.molo.core.designsystem.component.molecule.CommunityArticleCard
import news.molo.core.designsystem.component.molecule.NewsArticleCard
import news.molo.core.designsystem.component.molecule.OfficialArticleCard
import news.molo.core.designsystem.component.molecule.PublishedStyle
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.domain.model.Category

@Composable
fun FeedArticleItem(
    article: Article,
    onArticleClick: (Article) -> Unit,
    onPublisherClick: (Int) -> Unit,
    onClickDelete: (() -> Unit)? = null
) {
    when (article.category) {
        Category.NEWS -> NewsArticleCard(
            onClick = { onArticleClick(article) },
            title = article.title,
            content = article.content,
            imageURL = article.imageURL,
            organizationImageURL = article.organization.imageURL,
            organizationName = article.organization.name,
            createdAt = article.createdAt,
            viewCount = article.viewCount,
            bookmarkCount = article.bookmarkCount,
            onOrganizationClicked = { onPublisherClick(article.organization.id) },
            onClickDelete = onClickDelete
        )

        Category.OFFICIAL -> OfficialArticleCard(
            onClick = { onArticleClick(article) },
            title = article.title,
            content = article.content,
            imageURL = article.imageURL,
            organizationImageURL = article.organization.imageURL,
            organizationName = article.organization.name,
            createdAt = article.createdAt,
            viewCount = article.viewCount,
            bookmarkCount = article.bookmarkCount,
            onOrganizationClicked = { onPublisherClick(article.organization.id) },
            onClickDelete = onClickDelete
        )

        Category.COMMUNITY -> CommunityArticleCard(
            headline = article.title,
            content = article.content,
            imageURL = article.imageURL,
            organizationImageURL = article.organization.imageURL,
            organizationName = article.organization.name,
            publishedStyle = PublishedStyle.Date(article.createdAt),
            viewCount = article.viewCount,
            bookmarkCount = article.bookmarkCount,
            onClick = { onArticleClick(article) },
            onPublisherClicked = { onPublisherClick((article.organization.id)) },
            onClickDelete = onClickDelete,
            isDraft = article.isDraft
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        FeedArticleItem(
            article = Article.mock,
            onArticleClick = { },
            onPublisherClick = { }
        )
    }
}
