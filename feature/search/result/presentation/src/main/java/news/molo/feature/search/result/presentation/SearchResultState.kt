package news.molo.feature.search.result.presentation

data class SearchResultState(
    val query: String,
    val tabs: List<SearchResultType> = SearchResultType.entries,
    val selectedTab: SearchResultType = SearchResultType.Article
) {

    sealed interface Intent {
        data object NavigateBack : Intent

        data class SetQuery(val query: String) : Intent

        data class SetSelectedTab(val tab: SearchResultType) : Intent

        data class ToEventDetail(val eventId: Int) : Intent

        data class ToOrganisation(val organisationId: Int) : Intent

        data class ToArticleDetails(val articleId: Int) : Intent

        data object RefreshEvents : Intent
        data object RefreshArticles : Intent
    }
}