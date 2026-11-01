package news.molo.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleCreatedResponse(
    @SerialName("id")
    val articleId: Int
)
