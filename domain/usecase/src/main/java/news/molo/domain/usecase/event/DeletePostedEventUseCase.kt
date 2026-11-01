package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class DeletePostedEventUseCase(
    private val eventRepository: EventRepository,
    sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<Int, Unit>(sessionRepository, deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Int
    ) {
        val response = eventRepository.deletePostedEvent(bearer = bearer, eventId = param)
        emitFrom(response) {
            MoloError.DeleteEventFailed
        }
    }
}