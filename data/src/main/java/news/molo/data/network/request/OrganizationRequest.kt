package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganizationRequest(
    @SerialName("organization_id")
    val organizationId: Int
)