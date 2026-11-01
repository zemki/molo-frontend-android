package news.molo.domain.model

import java.time.OffsetDateTime

data class Article(
    val id: Int,
    val link: String,
    val title: String,
    val content: String,
    val imageURL: String,
    val imageSource: String,
    val organization: Organization,
    val category: Category,
    val isBookmarked: Boolean,
    val createdAt: OffsetDateTime,
    val tags: List<Tag>,
    val areas: List<Area>,
    val viewCount: Int? = null,
    val bookmarkCount: Int? = null,
    val isDraft: Boolean
) {
    companion object {
        val mock = Article(
            id = 1,
            link = "https://convelop.de/",
            title = "Neues Projekt: molo bekommt ein Redesign",
            content = "Alt bekanntest Projekt 'molo' wird modernisiert. Alt bekanntest Projekt 'molo' wird modernisiert. Alt bekanntest Projekt 'molo' wird modernisiert. Alt bekanntest Projekt 'molo' wird modernisiert.",
            imageURL = "Test",
            organization = Organization.Companion.mockOrganization,
            category = Category.OFFICIAL,
            isBookmarked = false,
            createdAt = OffsetDateTime.now(),
            imageSource = "imagesource",
            tags = listOf(Tag.mock),
            areas = emptyList(),
            isDraft = true
        )
    }
}
