package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import news.molo.data.network.serializer.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class OccurrencesResponse(
    @SerialName("end_datetime")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDatetime: OffsetDateTime,
    @SerialName("start_datetime")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDatetime: OffsetDateTime
)