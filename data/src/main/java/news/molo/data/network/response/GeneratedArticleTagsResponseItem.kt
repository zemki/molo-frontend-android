package news.molo.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneratedArticleTagsResponseItem(
    @SerialName("id")
    val id: Int
)