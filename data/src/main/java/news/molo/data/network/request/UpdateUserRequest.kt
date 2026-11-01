package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    @SerialName("area")
    val area: Int,
    @SerialName("description")
    val description: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("homepage")
    val homepage: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("username")
    val username: String,
)