package news.molo.domain.usecase.article

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.mapLatest
import news.molo.domain.model.Article
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetMyPaginatedArticlePostsUseCase(
    private val articleRepository: ArticleRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<GetMyPaginatedArticlePostsUseCase.Params, List<Article>>(
    sessionRepository,
    deviceIdProvider
) {

    data class Params(
        val pageSize: Int,
        val offset: Int
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<List<Article>, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Params
    ) {
        sessionRepository.getProfileReloadTriggerAsFlow()
            .mapLatest {
                articleRepository.fetchArticlesByUser(
                    deviceId = deviceId,
                    bearer = bearer,
                    offset = param.offset,
                    pageSize = param.pageSize,
                )
            }.collect { emitFrom(it) { MoloError.ArticleFetchFailed } }
    }
}