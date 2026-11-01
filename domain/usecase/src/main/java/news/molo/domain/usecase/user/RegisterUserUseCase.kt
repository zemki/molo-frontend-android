package news.molo.domain.usecase.user

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.UserRegistration
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    deviceIdProvider: DeviceIdProvider,
) : DeviceIDAwareOutcomeUseCase<UserRegistration, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: UserRegistration
    ) {
        emitFrom(userRepository.registerUser(deviceId = deviceId, user = param)) {
            MoloError.RegistrationFailed
        }
    }
}