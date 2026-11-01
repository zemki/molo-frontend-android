package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.Article
import news.molo.domain.model.ArticleCreationData
import java.io.File

interface ArticleRepository {

    suspend fun fetchArticles(
        deviceId: String,
        query: String,
        pageSize: Int,
        offset: Int,
        ignoreUserSelection: Boolean,
        official: Boolean = true,
        community: Boolean = true,
        media: Boolean = true
    ): ApiResponse<List<Article>>

    suspend fun fetchArticlesForOrganization(
        deviceId: String,
        organizationId: Int,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Article>>

    suspend fun fetchBookmarkedArticles(
        deviceId: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Article>>

    suspend fun fetchArticlesByUser(
        deviceId: String,
        bearer: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Article>>

    suspend fun bookmarkArticle(deviceId: String, articleId: Int): ApiResponse<Unit>
    suspend fun removeBookmark(deviceId: String, articleId: Int): ApiResponse<Unit>
    suspend fun fetchArticleDetails(deviceId: String, articleId: Int): ApiResponse<Article>
    suspend fun flagArticle(deviceId: String, articleId: Int): ApiResponse<Unit>
    fun observeBookmarkSelection(): Flow<List<Int>>
    suspend fun cacheBookmarkedArticle(articleId: Int)
    suspend fun removeBookmarkedArticleFromCache(articleId: Int)

    suspend fun deletePostedArticle(bearer: String, articleId: Int): ApiResponse<Unit>

    suspend fun generateArticleTags(title: String, content: String): ApiResponse<List<Int>>

    suspend fun updateArticle(bearer: String, article: ArticleCreationData): ApiResponse<Unit>

    suspend fun createArticle(bearer: String, article: ArticleCreationData): ApiResponse<Int>

    suspend fun uploadArticlePicture(
        bearer: String,
        articleId: Int,
        file: File,
        source: String,
    ): ApiResponse<Unit>
}