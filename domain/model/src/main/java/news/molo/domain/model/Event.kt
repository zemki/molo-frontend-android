package news.molo.domain.model

import java.time.OffsetDateTime

data class Event(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val eventDate: OffsetDateTime = OffsetDateTime.now(),
    val eventStartDate: OffsetDateTime = OffsetDateTime.now(),
    val eventEndDate: OffsetDateTime = OffsetDateTime.now(),
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val eventLocation: String = "",
    val link: String = "",
    val isBookmarked: Boolean = false,
    val tags: List<Tag> = listOf(),
    val organization: Int = 0,
    val organizationName: String = "",
    val organizationImageUrl: String? = null,
    val imageURL: String = "",
    val imageSourceName: String = "",
    val occurrences: List<EventOccurrence> = emptyList(),
    val ticketURL: String = "",
    val town: String = "",
    val zipCode: String = "",
    val areas: List<Area>,
    val viewCount: Int? = null,
    val bookmarkCount: Int? = null,
    val street: String,
    val isDraft: Boolean = false,
) {

    val hasLocation: Boolean
        get() = latitude != 0.0 && longitude != 0.0

    val hasTicket: Boolean
        get() = ticketURL.isNotBlank()

    companion object {
        val mock = Event(
            id = 1,
            title = "Das ist ein cooler Titel für ein Event",
            content = "Nächste Woche gibts ein cooles Event hier in Bremen, aber wann genau weiß man nicht. Es ist geheim, checkst du?",
            eventDate = OffsetDateTime.now(),
            eventStartDate = OffsetDateTime.now(),
            eventEndDate = OffsetDateTime.now(),
            address = "Keiner Weiß Straße 196",
            eventLocation = "Bremen Area21",
            link = "www.bremen-unknown.de",
            isBookmarked = false,
            tags = emptyList(),
            organization = 1,
            organizationName = "Bremen Unknown",
            imageURL = "http://google.com",
            imageSourceName = "",
            occurrences = emptyList(),
            latitude = 53.073635,
            longitude = 8.806422,
            town = "Bremen",
            areas = emptyList(),
            street = "street mock"
        )
    }
}