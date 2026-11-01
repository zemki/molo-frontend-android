package news.molo.domain.usecase.article

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Article
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetPaginatedArticlesForOrganizationUseCase(
    private val articleRepository: ArticleRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GetPaginatedArticlesForOrganizationUseCase.Params, List<Article>>(
    deviceIdProvider
) {

    data class Params(
        val organisationId: Int,
        val pageSize: Int,
        val offset: Int
    )

    override suspend fun FlowCollector<Outcome<List<Article>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = articleRepository.fetchArticlesForOrganization(
            deviceId = deviceId,
            offset = param.offset,
            pageSize = param.pageSize,
            organizationId = param.organisationId
        )

        emitFrom(response) {
            MoloError.ArticleFetchFailed
        }
    }

}