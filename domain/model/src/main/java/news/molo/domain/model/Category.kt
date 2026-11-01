package news.molo.domain.model

enum class Category(val queryName: String) {
    NEWS("news"),
    COMMUNITY("collective"),
    OFFICIAL("official");

    companion object {
        fun fromQueryValue(value: String): Category = entries.firstOrNull { it.queryName == value } ?: NEWS
    }
}