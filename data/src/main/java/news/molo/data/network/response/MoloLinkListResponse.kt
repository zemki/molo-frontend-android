package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoloLinkListResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("results")
    val results: List<MoloLinkResponse>
)