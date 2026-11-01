package news.molo.domain.usecase.event

import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.map
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Event
import news.molo.domain.model.TagCategory
import news.molo.domain.model.withSelectedTags
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetEventUpdateDataUseCase(
    private val eventRepository: EventRepository,
    private val tagRepository: TagRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Pair<Event, List<TagCategory>>>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Pair<Event, List<TagCategory>>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = eventRepository.fetchEventDetails(deviceId, param)
            .flatMap { event ->
                tagRepository.fetchEventTagCategories(deviceId).map { tags ->
                    val selectedIds = event.tags.map { it.id }
                    event to tags.withSelectedTags { tag -> selectedIds.contains(tag.id) }
                }
            }
        emitFrom(response) {
            MoloError.EventLoadingFailed
        }
    }
}