package news.molo.domain.usecase.area

import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.Area
import news.molo.domain.model.AreaWithRadius
import news.molo.domain.model.UserLocation
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class SelectAreaUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<SelectAreaUseCase.Params, Unit>(deviceIdProvider) {

    data class Params(
        val area: Area,
        val radius: Int,
    )

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Params
    ) {
        val (area, radius) = param


        val response = userRepository.submitLocation(
            deviceId = deviceId,
            location = UserLocation(
                latitude = area.locationLatitude,
                longitude = area.locationLongitude,
                areaId = area.id,
                distance = radius
            )
        ).onSuccess {
            sessionRepository.setSelectedLocationWithRadius(
                AreaWithRadius(area, radius)
            )
        }

        emitFrom(response) {
            MoloError.AreaSubmitFailed
        }
    }
}