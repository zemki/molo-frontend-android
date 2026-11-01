package news.molo.domain.usecase.article

import de.convelop.forge.network.response.ApiResponse
import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.ArticleCreationData
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.provider.FileProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class UpdateArticleUseCase(
    private val articleRepository: ArticleRepository,
    private val sessionRepository: SessionRepository,
    private val fileProvider: FileProvider,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<UpdateArticleUseCase.Params, Unit>(
    sessionRepository,
    deviceIdProvider
) {

    data class Params(
        val article: ArticleCreationData,
        val pictureUrl: String? = null,
        val pictureSource: String = ""
    )

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Params
    ) {
        val response = articleRepository.updateArticle(bearer = bearer, article = param.article)
            .flatMap { uploadImageIfRequired(bearer, param) }
            .onSuccess { sessionRepository.triggerPostingReload() }

        emitFrom(response) {
            MoloError.ArticleUpdateFailed
        }
    }

    private suspend fun uploadImageIfRequired(
        bearer: String,
        param: Params
    ) = if (param.pictureUrl.isNullOrEmpty()) {
        ApiResponse.Success(Unit)
    } else {
        val file = fileProvider.resolveFile(param.pictureUrl).getOrNull()

        if (file != null) {
            articleRepository.uploadArticlePicture(
                bearer = bearer,
                articleId = param.article.id,
                file = file,
                source = param.pictureSource
            )
        } else {
            ApiResponse.Error.SerializationError(reason = "File not found")
        }
    }
}