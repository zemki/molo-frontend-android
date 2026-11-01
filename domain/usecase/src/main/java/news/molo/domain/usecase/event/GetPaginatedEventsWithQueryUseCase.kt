package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Event
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetPaginatedEventsWithQueryUseCase(
    private val eventRepository: EventRepository,
    sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<GetPaginatedEventsWithQueryUseCase.Params, List<Event>>(
    sessionRepository,
    deviceIdProvider
) {

    data class Params(
        val query: String,
        val pageSize: Int,
        val offset: Int
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<List<Event>, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Params
    ) {
        val response = eventRepository.fetchEventsForQuery(
            deviceId = deviceId,
            query = param.query,
            offset = param.offset,
            pageSize = param.pageSize,
        )

        emitFrom(response) { MoloError.EventLoadingFailed }
    }
}