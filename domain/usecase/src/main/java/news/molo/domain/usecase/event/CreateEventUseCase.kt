package news.molo.domain.usecase.event

import de.convelop.forge.network.response.ApiResponse
import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.EventCreationData
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.provider.FileProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class CreateEventUseCase(
    private val eventRepository: EventRepository,
    private val sessionRepository: SessionRepository,
    private val fileProvider: FileProvider,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<CreateEventUseCase.Params, Unit>(sessionRepository, deviceIdProvider) {

    data class Params(
        val event: EventCreationData,
        val pictureUrl: String? = null,
        val pictureSource: String = ""
    )

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Params
    ) {
        val response = eventRepository.createEvent(bearer = bearer, event = param.event)
            .flatMap {
                uploadImageIfRequired(
                    bearer,
                    param.copy(event = param.event.copy(id = it))
                )
            }
            .onSuccess { sessionRepository.triggerPostingReload() }

        emitFrom(response) {
            MoloError.EventCreationFailed
        }
    }

    private suspend fun uploadImageIfRequired(
        bearer: String,
        param: Params
    ) = if (param.pictureUrl.isNullOrEmpty()) {
        ApiResponse.Success(Unit)
    } else {
        val file = fileProvider.resolveFile(param.pictureUrl).getOrNull()

        if (file != null) {
            eventRepository.uploadEventPicture(
                bearer = bearer,
                eventId = param.event.id,
                file = file,
                source = param.pictureSource
            )
        } else {
            ApiResponse.Error.SerializationError(reason = "File not found")
        }
    }
}