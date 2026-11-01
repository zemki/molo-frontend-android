package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("access")
    val access: String,
    @SerialName("refresh")
    val refresh: String,
    @SerialName("user_id")
    val userId: Int
)