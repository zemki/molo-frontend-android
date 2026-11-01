package news.molo.domain.usecase.area

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Area
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.AreaRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetAreasUseCase(
    private val areaRepository: AreaRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<String, List<Area>>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<List<Area>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: String
    ) {
        val response = areaRepository.fetchAreas(query = param, deviceId = deviceId)
        emitFrom(response) {
            MoloError.AreaLoadingFailed
        }
    }
}