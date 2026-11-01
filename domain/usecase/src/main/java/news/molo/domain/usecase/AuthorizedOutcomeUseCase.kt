package news.molo.domain.usecase

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.error.MoloError

abstract class AuthorizedOutcomeUseCase<Param, Type : Any>(
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Param, Type>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Type, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Param
    ) {

        val bearer = sessionRepository.getAuthToken().getOrNull() ?: run {
           println("UseCase got called without token... Ignoring")
            emitFailure(MoloError.Unauthenticated)
            return
        }

        executeWithBearer(bearer = bearer, deviceId = deviceId, param = param)
    }

    protected abstract suspend fun FlowCollector<Outcome<Type, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Param
    )
}