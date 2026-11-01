package news.molo.domain.usecase.article

import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.TagCategory
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class UpdateArticleTagSelectionUseCase(
    private val tagRepository: TagRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<List<TagCategory>, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: List<TagCategory>
    ) {
        val tags = param.flatMap { it.tags }
        val selectedTagIds = tags.filter { it.isSelected }.map { it.id }

        val response = tagRepository.updateArticleTagSelection(
            deviceId = deviceId,
            tags = selectedTagIds
        ).onSuccess {
            tagRepository.cacheTags(*tags.toTypedArray())
        }

        emitFrom(response) {
            MoloError.TagSelectionFailed
        }
    }
}