package news.molo.domain.usecase.event

import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class RemoveEventBookmarkUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = eventRepository.removeBookmark(deviceId, param)
            .onSuccess { eventRepository.removeBookmarkedEventFromCache(param) }

        emitFrom(response) {
            MoloError.BookmarkUpdateFailed
        }
    }
}