package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoloLinkResponse(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)