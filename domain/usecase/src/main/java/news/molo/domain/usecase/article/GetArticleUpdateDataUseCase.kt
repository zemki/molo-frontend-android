package news.molo.domain.usecase.article

import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.map
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Article
import news.molo.domain.model.TagCategory
import news.molo.domain.model.withSelectedTags
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetArticleUpdateDataUseCase(
    private val articleRepository: ArticleRepository,
    private val tagRepository: TagRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Pair<Article, List<TagCategory>>>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Pair<Article, List<TagCategory>>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = articleRepository.fetchArticleDetails(deviceId, param)
            .flatMap { article ->
                tagRepository.fetchArticleTagCategories(deviceId = deviceId).map { tags ->
                    val selectedIds = article.tags.map { it.id }
                    article to tags.withSelectedTags { tag -> selectedIds.contains(tag.id) }
                }
            }
        emitFrom(response) {
            MoloError.ArticleFetchFailed
        }
    }
}