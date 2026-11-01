package news.molo.domain.usecase.article

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Article
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetPaginatedArticlesWithQueryUseCase(
    private val articleRepository: ArticleRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GetPaginatedArticlesWithQueryUseCase.Params, List<Article>>(
    deviceIdProvider
) {

    data class Params(
        val query: String = "",
        val pageSize: Int,
        val offset: Int
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<List<Article>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {

        val response = articleRepository.fetchArticles(
            deviceId = deviceId,
            offset = param.offset,
            pageSize = param.pageSize,
            query = param.query,
            ignoreUserSelection = true
        )
        emitFrom(response) { MoloError.ArticleFetchFailed }
    }
}