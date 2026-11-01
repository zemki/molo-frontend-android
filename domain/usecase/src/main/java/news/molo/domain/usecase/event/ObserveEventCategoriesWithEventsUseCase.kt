package news.molo.domain.usecase.event

import de.convelop.forge.network.response.map
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.combine
import news.molo.domain.model.EventCategory
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class ObserveEventCategoriesWithEventsUseCase(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val tagRepository: TagRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Unit, List<EventCategory>>(deviceIdProvider) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<List<EventCategory>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {

        combine(
            sessionRepository.getSelectedLocationWithRadiusAsFlow(),
            tagRepository.getAllTagsFromCache(),
            sessionRepository.getPostingRefreshAsFlow()
        ) { area, tags, _ ->
            eventRepository.fetchEventCategories(deviceId = deviceId)
                .map { categories -> categories.filter { it.eventCount > 0 } }
        }.collect { response ->
            emitFrom(response) {
                MoloError.TagSelectionFailed
            }
        }
    }
}