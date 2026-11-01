package news.molo.domain.model

enum class ArticleCategory(val queryName: String) {
    NEWS("news"),
    COMMUNITY("collective"),
    OFFICIAL("official")
}