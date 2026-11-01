package news.molo.domain.usecase.area

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Area
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.AreaRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetAreasByDistanceUseCase(
    private val areaRepository: AreaRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<GetAreasByDistanceUseCase.Params, List<Area>>(deviceIdProvider) {

    data class Params(
        val latitude: Double,
        val longitude: Double,
        val radius: Int,
    )

    override suspend fun FlowCollector<Outcome<List<Area>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val response = areaRepository.fetchAreasByRadius(
            deviceId = deviceId,
            latitude = param.latitude.toString(),
            longitude = param.longitude.toString(),
            radius = param.radius
        )

        emitFrom(response) {
            MoloError.NoAreaSelected
        }
    }
}