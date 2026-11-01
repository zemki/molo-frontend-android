package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    @SerialName("content")
    val content: String,
    @SerialName("event_location")
    val eventLocation: String,
    @SerialName("event_tags")
    val eventTags: List<Int>,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("link")
    val link: String,
    @SerialName("ticket_url")
    val ticketURL: String?,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("occurrences")
    val occurrences: List<OccurrenceRequest>,
    @SerialName("street")
    val street: String,
    @SerialName("title")
    val title: String,
    @SerialName("town")
    val town: String,
    @SerialName("zip_code")
    val zipCode: String,
    @SerialName("published")
    val published: Boolean
)