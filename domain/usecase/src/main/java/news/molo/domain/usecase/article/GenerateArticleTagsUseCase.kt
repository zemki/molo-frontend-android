package news.molo.domain.usecase.article

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GenerateArticleTagsUseCase(
    private val articleRepository: ArticleRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GenerateArticleTagsUseCase.Params, List<Int>>(deviceIdProvider) {

    data class Params(
        val title: String,
        val content: String
    )

    override suspend fun FlowCollector<Outcome<List<Int>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = articleRepository.generateArticleTags(
            title = param.title,
            content = param.content
        )

        emitFrom(response) {
            MoloError.TagSelectionFailed
        }
    }
}