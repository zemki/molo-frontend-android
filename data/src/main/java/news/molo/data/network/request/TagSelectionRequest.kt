package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagSelectionRequest(
    @SerialName("tag_ids")
    val tags: List<Int>
)