package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlagEventRequest(
    @SerialName("event_id")
    val eventId: Int
)