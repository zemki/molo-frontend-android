package news.molo.domain.usecase.event

import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class BookmarkEventUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = eventRepository.bookmarkEvent(deviceId, param)
            .onSuccess { eventRepository.cacheBookmarkedEvent(param) }

        emitFrom(response) {
            MoloError.BookmarkUpdateFailed
        }
    }
}