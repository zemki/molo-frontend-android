package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Event
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetPaginatedUpcomingEventsForOrganizationUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GetPaginatedUpcomingEventsForOrganizationUseCase.Params, List<Event>>(
    deviceIdProvider
) {

    data class Params(
        val pageSize: Int,
        val offset: Int,
        val organisationId: Int,
    )

    override suspend fun FlowCollector<Outcome<List<Event>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = eventRepository.fetchEventsForOrganization(
            deviceId = deviceId,
            offset = param.offset,
            pageSize = param.pageSize,
            organizationId = param.organisationId
        )

        emitFrom(response) {
            MoloError.ArticleFetchFailed
        }
    }
}