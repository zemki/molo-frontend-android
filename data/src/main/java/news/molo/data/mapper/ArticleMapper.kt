package news.molo.data.mapper

import news.molo.data.network.response.ArticleResponse
import news.molo.domain.model.Article
import news.molo.domain.model.Category
import news.molo.domain.model.Organization

fun ArticleResponse.toArticle(): Article = Article(
    id = this.id,
    link = this.link,
    title = this.title,
    isBookmarked = this.bookmarked,
    content = this.abstract,
    imageURL = this.imageUrl.orEmpty(),
    organization = Organization(
        id = this.organization,
        name = this.organizationName,
        imageURL = this.organizationImage.orEmpty(),
        category = Category.fromQueryValue(this.organizationType),
        isOwnedBySelfUser = false
    ),
    category = Category.fromQueryValue(this.organizationType),
    createdAt = this.date,
    imageSource = this.imageSource ?: "",
    tags = this.tags.map { it.toTag() },
    areas = this.areas.map { it.toArea() },
    viewCount = this.requestCount,
    bookmarkCount = this.bookmarkCount,
    isDraft = !this.published
)