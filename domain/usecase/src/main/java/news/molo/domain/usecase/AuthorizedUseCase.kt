package news.molo.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository

abstract class AuthorizedUseCase<Param, Type : Any>(
    deviceIdProvider: DeviceIdProvider,
    private val sessionRepository: SessionRepository
) : DeviceIDAwareUseCase<Param, Type>(deviceIdProvider) {

    override fun executeWithDeviceId(deviceId: String, param: Param): Flow<Type> = flow {
        val bearer = sessionRepository.getAuthToken().getOrNull() ?: run {
            println("UseCase got called without token... Ignoring")
            return@flow
        }
        emitAll(executeWithBearer(bearer, deviceId, param))
    }

    protected abstract fun executeWithBearer(bearer: String, deviceId: String, param: Param): Flow<Type>
}