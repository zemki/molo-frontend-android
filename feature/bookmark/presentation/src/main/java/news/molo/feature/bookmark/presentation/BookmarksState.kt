package news.molo.feature.bookmark.presentation

data class BookmarksState(
    val tabs: List<BookmarkType> = BookmarkType.entries,
    val selectedTab: BookmarkType = BookmarkType.Events
) {

    sealed interface Intent {
        data class SetSelectedTab(val tab: BookmarkType) : Intent

        data class ToEventDetail(val eventId: Int) : Intent
        data class ToOrganisation(val organisationId: Int) : Intent
        data class ToArticleDetails(val articleId: Int) : Intent
    }
}