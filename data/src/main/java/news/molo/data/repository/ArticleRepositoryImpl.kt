package news.molo.data.repository

import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonObject
import news.molo.core.util.helper.formatToHtml
import news.molo.data.database.dao.BookmarkedArticleIdDao
import news.molo.data.database.entity.BookmarkedArticleId
import news.molo.data.mapper.toArticle
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.request.ArticleRequest
import news.molo.data.network.request.FlagArticleRequest
import news.molo.data.network.response.ArticleCreatedResponse
import news.molo.data.network.response.ArticleListResponse
import news.molo.data.network.response.ArticleResponse
import news.molo.data.network.response.GeneratedArticleTagsResponseItem
import news.molo.domain.model.Article
import news.molo.domain.model.ArticleCreationData
import news.molo.domain.repository.ArticleRepository
import java.io.File

class ArticleRepositoryImpl(
    private val httpClient: HttpClient,
    private val bookmarkedArticleIdDao: BookmarkedArticleIdDao
) : ArticleRepository {

    override suspend fun fetchArticles(
        deviceId: String,
        query: String,
        pageSize: Int,
        offset: Int,
        ignoreUserSelection: Boolean,
        official: Boolean,
        community: Boolean,
        media: Boolean
    ): ApiResponse<List<Article>> = httpClient.execute<ArticleListResponse, List<Article>>(
        mapper = { response ->
            response.results.map { it.toArticle() }
        },
        requestBuilder = {
            get(Endpoint.User.ListArticle)
            header(HeaderValue.DeviceID(deviceId))

            parameters {
                putAll(
                    mapOf(
                        "limit" to pageSize.toString(),
                        "offset" to offset.toString(),
                        "news" to media.toString(),
                        "official" to official.toString(),
                        "collective" to community.toString(),
                        "ignore_user_selection" to ignoreUserSelection.toString(),
                    )
                )

                if (query.isNotBlank()) {
                    put("search", query)
                }
            }
        }
    )

    override suspend fun fetchArticlesForOrganization(
        deviceId: String,
        organizationId: Int,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Article>> = httpClient.execute<ArticleListResponse, List<Article>>(
        mapper = { response ->
            response.results.map { it.toArticle() }
        },
        requestBuilder = {
            get(Endpoint.User.ListArticleByOrganization)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                putAll(
                    mapOf(
                        "limit" to pageSize.toString(),
                        "offset" to offset.toString(),
                        "news" to true.toString(),
                        "official" to true.toString(),
                        "collective" to true.toString(),
                        "organization_id" to organizationId.toString()
                    )
                )
            }
        }
    )

    override suspend fun fetchBookmarkedArticles(
        deviceId: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Article>> = httpClient.execute<ArticleListResponse, List<Article>>(
        mapper = { response ->
            response.results.map { it.toArticle() }
        },
        requestBuilder = {
            get(Endpoint.User.ArticleBookmarks)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                put("limit", pageSize.toString())
                put("offset", offset.toString())
            }
        }
    )

    override suspend fun fetchArticlesByUser(
        deviceId: String,
        bearer: String,
        pageSize: Int,
        offset: Int
    ): ApiResponse<List<Article>> = httpClient.execute<ArticleListResponse, List<Article>>(
        mapper = { response ->
            response.results.map { it.toArticle() }
        },
        requestBuilder = {
            get(Endpoint.User.Article)
            header(HeaderValue.DeviceID(deviceId))
            header(HeaderValue.Bearer(bearer))
            parameters {
                put("limit", pageSize.toString())
                put("offset", offset.toString())
            }
        }
    )

    override suspend fun bookmarkArticle(
        deviceId: String, articleId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            put(Endpoint.User.ArticleBookmark(articleId = articleId))
            header(HeaderValue.DeviceID(deviceId))
            body(JsonObject(emptyMap()))
        }
    )

    override suspend fun removeBookmark(
        deviceId: String,
        articleId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            delete(Endpoint.User.ArticleBookmark(articleId = articleId))
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun fetchArticleDetails(
        deviceId: String,
        articleId: Int
    ): ApiResponse<Article> = httpClient.execute<ArticleResponse, Article>(
        mapper = { response ->
            response.toArticle()
        },
        requestBuilder = {
            get(Endpoint.Article.path + "/$articleId")
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun flagArticle(
        deviceId: String,
        articleId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            post(Endpoint.General.FlagArticle)
            header(HeaderValue.DeviceID(deviceId))
            body(FlagArticleRequest(articleId))
        }
    )

    override fun observeBookmarkSelection(): Flow<List<Int>> =
        bookmarkedArticleIdDao.getAllBookmarkedArticle().map { it.map { it.id } }

    override suspend fun cacheBookmarkedArticle(articleId: Int) =
        bookmarkedArticleIdDao.insertBookmarkedArticleId(BookmarkedArticleId(articleId))

    override suspend fun removeBookmarkedArticleFromCache(articleId: Int) =
        bookmarkedArticleIdDao.deleteBookmarkedArticleId(articleId)

    override suspend fun deletePostedArticle(
        bearer: String,
        articleId: Int
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            delete(Endpoint.Article.path + "/$articleId")
            header(HeaderValue.Bearer(bearer))
        }
    )

    override suspend fun generateArticleTags(
        title: String,
        content: String
    ): ApiResponse<List<Int>> =
        httpClient.execute<List<GeneratedArticleTagsResponseItem>, List<Int>>(
            mapper = { response ->
                response.map { it.id }
            },
            requestBuilder = {
                post(Endpoint.AI.GenerateArticleTags)
                multipartBody {
                    add("title", title)
                    add("abstract", content)
                }
            }
        )

    override suspend fun updateArticle(
        bearer: String,
        article: ArticleCreationData
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            put(Endpoint.Article.path + "/${article.id}")
            header(HeaderValue.Bearer(bearer = bearer))
            body(
                ArticleRequest(
                    content = article.content.formatToHtml(),
                    articleTags = article.tags.map { it.id },
                    title = article.title,
                    published = article.asDraft.not(),
                    link = article.link,
                )
            )
        }
    )

    override suspend fun createArticle(
        bearer: String,
        article: ArticleCreationData
    ): ApiResponse<Int> = httpClient.execute<ArticleCreatedResponse, Int>(
        mapper = { it.articleId },
        requestBuilder = {
            post(Endpoint.Article)
            header(HeaderValue.Bearer(bearer = bearer))
            body(
                ArticleRequest(
                    content = article.content.formatToHtml(),
                    articleTags = article.tags.map { it.id },
                    title = article.title,
                    published = article.asDraft.not(),
                    link = article.link
                )
            )
        }
    )

    override suspend fun uploadArticlePicture(
        bearer: String,
        articleId: Int,
        file: File,
        source: String,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(
                Endpoint.Article.PictureUpload(articleId = articleId)
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