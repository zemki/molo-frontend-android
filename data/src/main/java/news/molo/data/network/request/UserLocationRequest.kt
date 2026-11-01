package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLocationRequest(
    @SerialName("area")
    val area: Int?,
    @SerialName("location_latitude")
    val locationLatitude: String?,
    @SerialName("location_longitude")
    val locationLongitude: String?
)