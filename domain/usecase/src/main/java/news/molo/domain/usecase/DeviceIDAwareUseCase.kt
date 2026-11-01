package news.molo.domain.usecase

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import news.molo.domain.provider.DeviceIdProvider

abstract class DeviceIDAwareUseCase<Param, Type : Any>(
    private val deviceIdProvider: DeviceIdProvider
) : FlowUseCase<Param, Type>() {

    override fun execute(params: Param): Flow<Type> = flow {
        val deviceId = deviceIdProvider.getDeviceId().getOrNull() ?: run {
            println("UseCase got called without DeviceID...")
            return@flow
        }

        emitAll(executeWithDeviceId(deviceId, params))
    }

    protected abstract fun executeWithDeviceId(deviceId: String, param: Param): Flow<Type>
}