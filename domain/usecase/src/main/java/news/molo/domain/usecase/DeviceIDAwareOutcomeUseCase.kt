package news.molo.domain.usecase

import de.convelop.forge.outcome.Outcome
import de.convelop.forge.usecase.FlowOutcomeUseCase
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.usecase.error.MoloError

abstract class DeviceIDAwareOutcomeUseCase<Param, Type : Any>(
    private val deviceIdProvider: DeviceIdProvider
) : FlowOutcomeUseCase<Param, Type, MoloError>(){

    override suspend fun FlowCollector<Outcome<Type, MoloError>>.execute(params: Param) {
        val deviceId = deviceIdProvider.getDeviceId().getOrNull() ?: run {
            println("UseCase got called without DeviceID...")
            emitFailure(MoloError.DeviceIdMissing)
            return
        }

        if (deviceId.isBlank()) {
            emitFailure(MoloError.DeviceIdMissing)
            return
        }

        executeWithDeviceId(deviceId, params)
    }

    protected abstract suspend fun FlowCollector<Outcome<Type, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Param
    )
}
