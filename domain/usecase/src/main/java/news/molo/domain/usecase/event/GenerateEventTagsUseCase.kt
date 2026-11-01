package news.molo.domain.usecase.event

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.EventRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GenerateEventTagsUseCase(
    private val eventRepository: EventRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GenerateEventTagsUseCase.Params, List<Int>>(deviceIdProvider) {

    data class Params(
        val title: String,
        val content: String
    )

    override suspend fun FlowCollector<Outcome<List<Int>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = eventRepository.generateEventTags(
            title = param.title,
            content = param.content
        )

        emitFrom(response) {
            MoloError.TagSelectionFailed
        }
    }
}