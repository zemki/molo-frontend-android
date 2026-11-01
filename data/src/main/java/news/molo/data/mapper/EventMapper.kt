package news.molo.data.mapper

import news.molo.core.util.helper.ensureHttps
import news.molo.data.network.response.EventResponse
import news.molo.data.network.response.OccurrencesResponse
import news.molo.domain.model.Event
import news.molo.domain.model.EventOccurrence

fun EventResponse.toEvent(): Event = Event(
    id = this.id,
    link = this.link?.ensureHttps() ?: "",
    title = this.title,
    content = this.content,
    imageURL = this.imageUrl?.ensureHttps().orEmpty(),
    isBookmarked = this.bookmarked,
    eventLocation = this.eventLocation ?: "",
    eventDate = this.startDate,
    eventStartDate = this.startDate,
    eventEndDate = this.endDate,
    organization = this.organization,
    organizationName = this.organizationName,
    organizationImageUrl = this.organizationImageURL,
    imageSourceName = this.imageSource ?: "",
    latitude = this.latitude?.toDoubleOrNull() ?: 0.0,
    longitude = this.longitude?.toDoubleOrNull() ?: 0.0,
    tags = this.tags.map { it.toTag() },
    ticketURL = this.ticketURL?.ensureHttps() ?: "",
    town = this.town,
    zipCode = this.zipCode ?: "",
    occurrences = this.occurrencesList?.map { it.toEventOccurrence() } ?: emptyList(),
    areas = this.area?.map { it.toArea() } ?: emptyList(),
    viewCount = this.requestCount,
    bookmarkCount = this.bookmarkedCount,
    street = this.street,
    isDraft = !published
)

fun OccurrencesResponse.toEventOccurrence(): EventOccurrence = EventOccurrence(
    startDate = this.startDatetime,
    endDate = this.endDatetime
)
