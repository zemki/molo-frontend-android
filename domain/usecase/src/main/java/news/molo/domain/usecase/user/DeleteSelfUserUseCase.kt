package news.molo.domain.usecase.user

import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class DeleteSelfUserUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : AuthorizedOutcomeUseCase<Unit, Unit>(sessionRepository, deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Unit
    ) {
        val response = userRepository.deleteSelfUser(bearer)
            .onSuccess { sessionRepository.deleteAuthToken() }

        emitFrom(response) {
            MoloError.UserNotLoggedIn
        }
    }
}