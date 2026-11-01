package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelectedOrganizationsRequest(
    @SerialName("organization")
    val organizationTags: List<Int> = emptyList()
)