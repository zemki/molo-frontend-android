package news.molo.data.repository

import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonObject
import news.molo.core.util.helper.formatToHtml
import news.molo.data.database.dao.BookmarkedEventDao
import news.molo.data.database.entity.BookmarkedEventId
import news.molo.data.mapper.toEvent
import news.molo.data.mapper.toEventCategory
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.request.CreateEventRequest
import news.molo.data.network.request.FlagEventRequest
import news.molo.data.network.request.OccurrenceRequest
import news.molo.data.network.response.ArticleCreatedResponse
import news.molo.data.network.response.EventCategoryResponse
import news.molo.data.network.response.EventListResponse
import news.molo.data.network.response.EventResponse
import news.molo.data.network.response.GeneratedArticleTagsResponseItem
import news.molo.data.network.response.GeneratedEventTagsResponseItem
import news.molo.domain.model.Event
import news.molo.domain.model.EventCategory
import news.molo.domain.model.EventCreationData
import news.molo.domain.repository.EventRepository
import java.io.File
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.map

class EventRepositoryImpl(
    private val httpClient: HttpClient,
    private val bookmarkedEventDao: BookmarkedEventDao
) : EventRepository {

    override suspend fun fetchEvents(
        deviceId: String,
        categoryId: Int,
        query: String,
        pageSize: Int,
        offset: Int,
        from: OffsetDateTime?,
        to: OffsetDateTime?
    ): ApiResponse<List<Event>> = httpClient.execute<EventListResponse, List<Event>>(
        mapper = { response ->
            response.results.map { it.toEvent() }
        },
        requestBuilder = {
            get(Endpoint.User.EventsByCategory)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                putAll(
                    mapOf(
                        "limit" to pageSize.toString(),
                        "offset" to offset.toString(),
                        "news" to true.toString(),
                        "official" to true.toString(),
                        "collective" to true.toString(),
                        "category_id" to categoryId.toString()
                    )
                )

                if (query.isNotBlank()) {
                    put("search", query)
                }

                from?.let { put("date_start", it.format(DateTimeFormatter.ISO_LOCAL_DATE)) }
                to?.let { put("date_end", it.format(DateTimeFormatter.ISO_LOCAL_DATE)) }
            }
        }
    )

    override suspend fun fetchEventsForQuery(
        deviceId: String,
        query: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>> = httpClient.execute<EventListResponse, List<Event>>(
        mapper = { response ->
            response.results.map { it.toEvent() }
        },
        requestBuilder = {
            get(Endpoint.Event)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                putAll(
                    mapOf(
                        "limit" to pageSize.toString(),
                        "offset" to offset.toString(),
                        "news" to true.toString(),
                        "official" to true.toString(),
                        "collective" to true.toString(),
                    )
                )

                if (query.isNotBlank()) {
                    put("search", query)
                }
            }
        }
    )

    override suspend fun fetchBookmarkedEvents(
        deviceId: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>> = httpClient.execute<EventListResponse, List<Event>>(
        mapper = { response ->
            response.results.map { it.toEvent() }
        },
        requestBuilder = {
            get(Endpoint.User.EventBookmarks)
            header(HeaderValue.DeviceID(deviceId))

            parameters {
                put("limit", pageSize.toString())
                put("offset", offset.toString())
            }
        }
    )

    override suspend fun fetchEventsByUser(
        deviceId: String,
        bearer: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>> = httpClient.execute<EventListResponse, List<Event>>(
        mapper = { response ->
            response.results.map { it.toEvent() }
        },
        requestBuilder = {
            get(Endpoint.User.Event)
            header(HeaderValue.DeviceID(deviceId))
            header(HeaderValue.Bearer(bearer))

            parameters {
                put("limit", pageSize.toString())
                put("offset", offset.toString())
            }
        }
    )

    override suspend fun fetchEventsForOrganization(
        deviceId: String,
        organizationId: Int,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Event>> = httpClient.execute<EventListResponse, List<Event>>(
        mapper = { response ->
            response.results.map { it.toEvent() }
        },
        requestBuilder = {
            get(Endpoint.User.ListEventsByOrganization)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                put("limit", pageSize.toString())
                put("offset", offset.toString())
                put("organization_id", organizationId.toString())
            }
        }
    )


    override suspend fun bookmarkEvent(
        deviceId: String,
        eventId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            put(Endpoint.User.EventBookmarks.path + "/$eventId")
            header(HeaderValue.DeviceID(deviceId))
            body(JsonObject(emptyMap()))
        }
    )

    override suspend fun removeBookmark(
        deviceId: String,
        eventId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            delete(Endpoint.User.EventBookmarks.path + "/$eventId")
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun fetchEventDetails(
        deviceId: String,
        eventId: Int
    ): ApiResponse<Event> = httpClient.execute<EventResponse, Event>(
        mapper = { response ->
            response.toEvent()
        },
        requestBuilder = {
            get(Endpoint.Event.path + "/$eventId")
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun flagEvent(
        deviceId: String,
        eventId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            post(Endpoint.General.FlagEvent)
            header(HeaderValue.DeviceID(deviceId))
            body(FlagEventRequest(eventId))
        }
    )

    override fun observeBookmarkSelection(): Flow<List<Int>> =
        bookmarkedEventDao.getAllBookmarkedEvents().map { it.map { it.id } }

    override suspend fun cacheBookmarkedEvent(eventId: Int) =
        bookmarkedEventDao.insertBookmarkedEventId(
            BookmarkedEventId(eventId)
        )

    override suspend fun removeBookmarkedEventFromCache(eventId: Int) =
        bookmarkedEventDao.deleteBookmarkedEventId(eventId)

    override suspend fun fetchEventCategories(
        deviceId: String
    ): ApiResponse<List<EventCategory>> =
        httpClient.execute<List<EventCategoryResponse>, List<EventCategory>>(
            mapper = { response ->
                response.map { it.toEventCategory() }
            },
            requestBuilder = {
                get(Endpoint.User.ListEventTag)
                header(HeaderValue.DeviceID(deviceId))
            }
        )

    override suspend fun deletePostedEvent(
        bearer: String,
        eventId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            delete(Endpoint.Event.path + "/$eventId")
            header(HeaderValue.Bearer(bearer))
        }
    )

    override suspend fun updateEvent(
        bearer: String,
        event: EventCreationData
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            put(Endpoint.Event.path + "/${event.id}")
            header(HeaderValue.Bearer(bearer = bearer))
            body(
                CreateEventRequest(
                    content = event.content.formatToHtml(),
                    eventLocation = event.eventLocation,
                    eventTags = event.tags.map { it.id },
                    latitude = event.latitude,
                    link = event.link,
                    longitude = event.longitude,
                    occurrences = listOf(
                        OccurrenceRequest(event.from, event.until)
                    ),
                    street = event.street,
                    title = event.title,
                    town = event.city,
                    zipCode = event.zip,
                    published = event.saveAsDraft.not(),
                    ticketURL = event.ticketLink
                )
            )
        }
    )

    override suspend fun createEvent(
        bearer: String,
        event: EventCreationData
    ): ApiResponse<Int> = httpClient.execute<ArticleCreatedResponse, Int>(
        mapper = { it.articleId },
        requestBuilder = {
            post(Endpoint.Event)
            header(HeaderValue.Bearer(bearer = bearer))
            body(
                CreateEventRequest(
                    content = event.content.formatToHtml(),
                    eventLocation = event.eventLocation,
                    eventTags = event.tags.map { it.id },
                    latitude = event.latitude,
                    link = event.link,
                    longitude = event.longitude,
                    occurrences = listOf(
                        OccurrenceRequest(event.from, event.until)
                    ),
                    street = event.street,
                    title = event.title,
                    town = event.city,
                    zipCode = event.zip,
                    published = event.saveAsDraft.not(),
                    ticketURL = event.ticketLink
                )
            )
        }
    )

    override suspend fun generateEventTags(
        title: String,
        content: String
    ): ApiResponse<List<Int>> =
        httpClient.execute<List<GeneratedEventTagsResponseItem>, List<Int>>(
            mapper = { response ->
                response.map { it.id }
            },
            requestBuilder = {
                post(Endpoint.AI.GenerateEventTags)
                multipartBody {
                    add("title", title)
                    add("abstract", content)
                }
            }
        )

    override suspend fun uploadEventPicture(
        bearer: String,
        eventId: Int,
        file: File,
        source: String,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(
                Endpoint.Event.PictureUpload(eventId = eventId)
            )
            header(HeaderValue.Bearer(bearer))
            multipartBody {
                addFile(
                    name = "picture",
                    filename = file.name,
                    content = file.readBytes(),
                    contentType = "octet-stream"
                )
                add("image_source", source)
            }
        }
    )
}