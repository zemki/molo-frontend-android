package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventListResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("results")
    val results: List<EventResponse>
)