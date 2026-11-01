package news.molo.domain.model

data class ArticleCreationData(
    val id: Int,
    val title: String,
    val content: String,
    val tags: List<Tag>,
    val asDraft: Boolean,
    val link: String
)
