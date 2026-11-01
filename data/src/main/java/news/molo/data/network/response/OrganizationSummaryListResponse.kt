package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganizationSummaryListResponse(
    @SerialName("overview")
    val overview: List<OrganizationSummaryResponse>
)