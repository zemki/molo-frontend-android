package news.molo.domain.usecase.area

import de.convelop.forge.network.response.ApiResponse
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Area
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.AreaRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetSelectedAreaUseCase(
    private val areaRepository: AreaRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Unit, Area>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Area, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {
        val areaName = sessionRepository.getSelectedAreaName().getOrNull()
            .orEmpty()
            .takeUnless { it.isEmpty() }
            ?: run { emitFailure(MoloError.NoAreaSelected); return }

        when (val result = areaRepository.fetchAreas(deviceId = deviceId)) {
            is ApiResponse.Error -> emitFailure(MoloError.AreaLoadingFailed)
            is ApiResponse.Success -> {

                val area = result.body.find { it.name == areaName } ?: run {
                    emitFailure(MoloError.AreaLoadingFailed)
                    return
                }

                emitSuccess(area)
            }
        }
    }
}