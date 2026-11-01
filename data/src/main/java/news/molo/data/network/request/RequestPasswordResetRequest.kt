package news.molo.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPasswordResetRequest(
    @SerialName("email")
    val email: String,
)