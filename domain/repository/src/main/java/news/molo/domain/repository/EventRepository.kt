package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.Event
import news.molo.domain.model.EventCategory
import news.molo.domain.model.EventCreationData
import java.io.File
import java.time.OffsetDateTime

interface EventRepository {
    suspend fun fetchEvents(
        deviceId: String,
        categoryId: Int,
        query: String,
        pageSize: Int,
        offset: Int,
        from: OffsetDateTime? = null,
        to: OffsetDateTime? = null
    ): ApiResponse<List<Event>>

    suspend fun fetchEventsForQuery(
        deviceId: String,
        query: String,
        pageSize: Int,
        offset: Int,
    ): ApiResponse<List<Event>>

    suspend fun fetchBookmarkedEvents(
        deviceId: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>>

    suspend fun fetchEventsByUser(
        deviceId: String,
        bearer: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>>

    suspend fun fetchEventsForOrganization(
        deviceId: String,
        organizationId: Int,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>>


    suspend fun bookmarkEvent(deviceId: String, eventId: Int): ApiResponse<Unit>
    suspend fun removeBookmark(deviceId: String, eventId: Int): ApiResponse<Unit>

    suspend fun fetchEventDetails(deviceId: String, eventId: Int): ApiResponse<Event>
    suspend fun flagEvent(deviceId: String, eventId: Int): ApiResponse<Unit>

    fun observeBookmarkSelection(): Flow<List<Int>>
    suspend fun cacheBookmarkedEvent(eventId: Int)
    suspend fun removeBookmarkedEventFromCache(eventId: Int)

    suspend fun fetchEventCategories(
        deviceId: String,
    ): ApiResponse<List<EventCategory>>

    suspend fun deletePostedEvent(bearer: String, eventId: Int): ApiResponse<Unit>

    suspend fun generateEventTags(title: String, content: String): ApiResponse<List<Int>>

    suspend fun updateEvent(bearer: String, event: EventCreationData): ApiResponse<Unit>

    suspend fun createEvent(bearer: String, event: EventCreationData): ApiResponse<Int>

    suspend fun uploadEventPicture(
        bearer: String,
        eventId: Int,
        file: File,
        source: String,
    ): ApiResponse<Unit>
}