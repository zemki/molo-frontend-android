package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("latitude")
    val latitude: String? = "0.0",
    @SerialName("longitude")
    val longitude: String? = "0.0",
    @SerialName("name")
    val name: String
)