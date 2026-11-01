package news.molo.domain.usecase.article

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Article
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetArticleDetailsUseCase(
    private val articleRepository: ArticleRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Article>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Article, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = articleRepository.fetchArticleDetails(deviceId, param)
        emitFrom(response) {
            MoloError.ArticleFetchFailed
        }
    }
}