package news.molo.domain.model

data class SearchHistoryEntry(
    val id: Int = 0,
    val query: String,
    val type: SearchHistoryType = SearchHistoryType.Text
)
