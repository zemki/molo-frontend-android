package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import news.molo.data.network.serializer.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class EventResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("area")
    val area: List<AreaResponse>?,
    @SerialName("bookmarked_count")
    val bookmarkedCount: Int?,
    @SerialName("content")
    val content: String,
    @SerialName("event_location")
    val eventLocation: String?,
    @SerialName("image_source")
    val imageSource: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("latitude")
    val latitude: String?,
    @SerialName("link")
    val link: String?,
    @SerialName("longitude")
    val longitude: String?,
    @SerialName("occurrences_list")
    val occurrencesList: List<OccurrencesResponse>?,
    @SerialName("organization")
    val organization: Int,
    @SerialName("organization_name")
    val organizationName: String,
    @SerialName("organization_type")
    val organizationType: String?,
    @SerialName("organization_image")
    val organizationImageURL: String?,
    @SerialName("request_count")
    val requestCount: Int?,
    @SerialName("start_date")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDate: OffsetDateTime,
    @SerialName("end_date")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDate: OffsetDateTime = OffsetDateTime.now(),
    @SerialName("street")
    val street: String,
    @SerialName("tags")
    val tags: List<TagResponse> = emptyList(),
    @SerialName("title")
    val title: String,
    @SerialName("town")
    val town: String,
    @SerialName("zip_code")
    val zipCode: String?,
    @SerialName("ticket_url")
    val ticketURL: String?,
    @SerialName("published")
    val published: Boolean = true,
    @SerialName("bookmarked")
    val bookmarked: Boolean = false
)