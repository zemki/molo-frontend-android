package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlagArticleRequest(
    @SerialName("article_id")
    val articleId: Int
)