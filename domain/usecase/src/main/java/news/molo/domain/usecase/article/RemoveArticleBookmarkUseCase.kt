package news.molo.domain.usecase.article

import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class RemoveArticleBookmarkUseCase(
    private val articleRepository: ArticleRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = articleRepository.removeBookmark(deviceId, param)
            .onSuccess { articleRepository.removeBookmarkedArticleFromCache(param) }

        emitFrom(response) {
            MoloError.BookmarkUpdateFailed
        }
    }
}