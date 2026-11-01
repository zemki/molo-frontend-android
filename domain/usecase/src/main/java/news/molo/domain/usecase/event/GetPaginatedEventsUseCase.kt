package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Event
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError
import java.time.OffsetDateTime

class GetPaginatedEventsUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GetPaginatedEventsUseCase.Params, List<Event>>(
    deviceIdProvider
) {

    data class Params(
        val pageSize: Int,
        val offset: Int,
        val categoryId: Int,
        val from: OffsetDateTime?,
        val to: OffsetDateTime?,
        val query: String = ""
    )

    override suspend fun FlowCollector<Outcome<List<Event>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = eventRepository.fetchEvents(
            deviceId = deviceId,
            offset = param.offset,
            pageSize = param.pageSize,
            categoryId = param.categoryId,
            query = param.query,
            from = param.from,
            to = param.to
        )

        emitFrom(response) {
            MoloError.ArticleFetchFailed
        }
    }
}
