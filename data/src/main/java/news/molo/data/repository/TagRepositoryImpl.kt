package news.molo.data.repository

import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import news.molo.data.database.dao.TagDao
import news.molo.data.mapper.toEntity
import news.molo.data.mapper.toTag
import news.molo.data.mapper.toTagCategory
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.request.TagSelectionRequest
import news.molo.data.network.response.TagCategoryResponse
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory
import news.molo.domain.repository.TagRepository

class TagRepositoryImpl(
    private val httpClient: HttpClient,
    private val tagDao: TagDao
) : TagRepository {

    override suspend fun fetchArticleTagCategories(
        deviceId: String
    ): ApiResponse<List<TagCategory>> = httpClient.execute<List<TagCategoryResponse>, List<TagCategory>>(
        mapper = { response ->
            response.map { it.toTagCategory() }
        },
        requestBuilder = {
            get(Endpoint.User.ListArticleTag)
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun updateArticleTagSelection(
        deviceId: String,
        tags: List<Int>
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User.SetArticleTags)
            header(HeaderValue.DeviceID(deviceId))
            body(TagSelectionRequest(tags = tags))
        }
    )

    override suspend fun fetchEventTagCategories(
        deviceId: String
    ): ApiResponse<List<TagCategory>> = httpClient.execute<List<TagCategoryResponse>, List<TagCategory>>(
        mapper = { response ->
            response.map { it.toTagCategory() }
        },
        requestBuilder = {
            get(Endpoint.User.ListEventTag)
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun updateEventTagSelection(
        deviceId: String,
        tags: List<Int>
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            post(Endpoint.User.SetEventTags)
            header(HeaderValue.DeviceID(deviceId))
            body(TagSelectionRequest(tags = tags))
        }
    )

    override suspend fun cacheTags(vararg tag: Tag) = tagDao.insertTags(tag.map { it.toEntity() })

    override fun getAllTagsFromCache(): Flow<List<Tag>> = tagDao.getAllTags().map { it.map { it.toTag() } }
}