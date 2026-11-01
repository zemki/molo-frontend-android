package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrganizationSummaryResponse(
    @SerialName("all_tags")
    val allTags: Int,
    @SerialName("by_tag")
    val byTag: Int,
    @SerialName("deselected")
    val deselected: Int,
    @SerialName("name")
    val name: String,
    @SerialName("push")
    val push: Boolean,
    @SerialName("type")
    val type: String
)