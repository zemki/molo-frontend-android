package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Event
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetPaginatedBookmarkedEventsUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GetPaginatedBookmarkedEventsUseCase.Params, List<Event>>(
    deviceIdProvider
) {

    data class Params(
        val pageSize: Int,
        val offset: Int
    )

    override suspend fun FlowCollector<Outcome<List<Event>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = eventRepository.fetchBookmarkedEvents(
            deviceId = deviceId,
            offset = param.offset,
            pageSize = param.pageSize,
        )

        emitFrom(response) {
            MoloError.ArticleFetchFailed
        }
    }
}