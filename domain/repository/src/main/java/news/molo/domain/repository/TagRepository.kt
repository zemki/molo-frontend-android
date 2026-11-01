package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory

interface TagRepository {

    suspend fun fetchArticleTagCategories(deviceId: String): ApiResponse<List<TagCategory>>
    suspend fun updateArticleTagSelection(deviceId: String, tags: List<Int>): ApiResponse<Unit>

    suspend fun fetchEventTagCategories(deviceId: String): ApiResponse<List<TagCategory>>
    suspend fun updateEventTagSelection(deviceId: String, tags: List<Int>): ApiResponse<Unit>
    suspend fun cacheTags(vararg tag: Tag)
    fun getAllTagsFromCache(): Flow<List<Tag>>
}