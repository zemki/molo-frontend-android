package news.molo.domain.model

import java.time.OffsetDateTime

data class EventOccurrence(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime
)
