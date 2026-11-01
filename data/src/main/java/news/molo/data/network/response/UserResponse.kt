package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("area")
    val area: AreaResponse,
    @SerialName("description")
    val description: String = "",
    @SerialName("display_name")
    val displayName: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("homepage")
    val homepage: String = "",
    @SerialName("image_source")
    val imageSource: String? = null,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone")
    val phone: String = "",
    @SerialName("picture")
    val picture: String? = null,
    @SerialName("street")
    val street: String = "",
    @SerialName("town")
    val town: String = "",
    @SerialName("username")
    val username: String,
    @SerialName("zip")
    val zip: String = "",
    @SerialName("organization_id")
    val organizationId: Int
)