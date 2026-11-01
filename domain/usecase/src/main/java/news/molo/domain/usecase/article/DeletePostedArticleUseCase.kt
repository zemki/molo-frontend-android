package news.molo.domain.usecase.article

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class DeletePostedArticleUseCase(
    private val articleRepository: ArticleRepository,
    sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<Int, Unit>(sessionRepository, deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Int
    ) {
        val response = articleRepository.deletePostedArticle(bearer = bearer, articleId = param)
        emitFrom(response) {
            MoloError.DeleteArticleFailed
        }
    }
}