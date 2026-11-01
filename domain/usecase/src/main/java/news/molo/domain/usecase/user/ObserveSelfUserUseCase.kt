package news.molo.domain.usecase.user

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import news.molo.domain.model.User
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class ObserveSelfUserUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Unit, User>(deviceIdProvider) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<User, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {
        sessionRepository.getAuthTokenAsFlow()
            .flatMapLatest { bearer ->
                sessionRepository.getProfileReloadTriggerAsFlow()
                    .map { bearer }
                    .onStart { emit(bearer) }
            }
            .map { bearer ->
                userRepository.fetchSelfUser(bearer = bearer)
            }
            .collect { result ->
                emitFrom(result) { MoloError.UserNotLoggedIn }
            }
    }
}