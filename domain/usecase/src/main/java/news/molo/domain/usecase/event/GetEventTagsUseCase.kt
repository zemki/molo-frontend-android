package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.TagCategory
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetEventTagsUseCase(
    private val tagRepository: TagRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Unit, List<TagCategory>>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<List<TagCategory>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {
        val response = tagRepository.fetchEventTagCategories(deviceId = deviceId)
        emitFrom(response) {
            MoloError.TagSelectionFailed
        }
    }
}