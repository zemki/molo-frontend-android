package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleRequest(
    @SerialName("abstract")
    val content: String,
    @SerialName("article_tags")
    val articleTags: List<Int>,
    @SerialName("title")
    val title: String,
    @SerialName("published")
    val published: Boolean,
    @SerialName("link")
    val link: String,
)