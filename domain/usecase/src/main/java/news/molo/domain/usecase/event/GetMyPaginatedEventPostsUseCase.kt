package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.mapLatest
import news.molo.domain.model.Event
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetMyPaginatedEventPostsUseCase(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<GetMyPaginatedEventPostsUseCase.Params, List<Event>>(
    sessionRepository,
    deviceIdProvider
) {

    data class Params(
        val pageSize: Int,
        val offset: Int
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<List<Event>, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Params
    ) {
        sessionRepository.getProfileReloadTriggerAsFlow()
            .mapLatest {
                eventRepository.fetchEventsByUser(
                    deviceId = deviceId,
                    bearer = bearer,
                    offset = param.offset,
                    pageSize = param.pageSize,
                )
            }.collect { emitFrom(it) { MoloError.EventLoadingFailed } }
    }
}
