package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Event
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetEventDetailsUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Event>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Event, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = eventRepository.fetchEventDetails(deviceId, param)
        emitFrom(response) {
            MoloError.EventLoadingFailed
        }
    }
}