package news.molo.feature.feed.presentation

data class FeedState(
    val query: String = "",
    val filter: List<ArticleFilter> = ArticleFilter.all(),
    val selectedFilter: ArticleFilter? = null,
) {

    sealed interface Intent {
        data object Refresh : Intent
        data class SelectFilter(val filter: ArticleFilter) : Intent
        data class Query(val query: String) : Intent
        data class ToArticleDetails(val articleId: Int) : Intent
        data class ToOrganisation(val organisationId: Int) : Intent
    }
}