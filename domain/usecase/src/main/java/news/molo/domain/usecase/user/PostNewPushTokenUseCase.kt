package news.molo.domain.usecase.user

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.provider.PushTokenProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class PostNewPushTokenUseCase(
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider,
    private val pushTokenProvider: PushTokenProvider,
    private val userRepository: UserRepository
) : DeviceIDAwareOutcomeUseCase<Unit, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {
        //val currentToken = sessionRepository.getPushToken().getOrDefault("")
        val pushToken = pushTokenProvider.getPushToken().getOrDefault("")

        //if (pushToken == currentToken) {
        //    return
        //}

        val response = userRepository.submitPushToken(deviceId, pushToken)
        emitFrom(response) {
            MoloError.PushTokenFetchFailed
        }
    }
}