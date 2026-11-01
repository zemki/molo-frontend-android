package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganizationResponse(
    @SerialName("address")
    val address: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String,
    @SerialName("is_selected")
    val isSelected: Boolean?,
    @SerialName("name")
    val name: String,
    @SerialName("title")
    val title: String?,
    @SerialName("type")
    val type: String
)