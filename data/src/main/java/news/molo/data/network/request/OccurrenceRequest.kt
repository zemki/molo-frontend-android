package news.molo.data.network.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import news.molo.data.network.serializer.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class OccurrenceRequest(
    @SerialName("event_start_date") @Serializable(with = OffsetDateTimeSerializer::class) val eventStartDate: OffsetDateTime,
    @SerialName("event_end_date") @Serializable(with = OffsetDateTimeSerializer::class) val eventEndDate: OffsetDateTime
)