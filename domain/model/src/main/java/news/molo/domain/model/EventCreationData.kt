package news.molo.domain.model

import java.time.OffsetDateTime

data class EventCreationData(
    val id: Int,
    val title: String,
    val content: String,
    val tags: List<Tag>,
    val from: OffsetDateTime,
    val until: OffsetDateTime,
    val street: String,
    val zip: String,
    val city: String,
    val eventLocation: String,
    val link: String,
    val ticketLink: String?,
    val latitude: Double,
    val longitude: Double,
    val saveAsDraft: Boolean = false
)