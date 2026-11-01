package news.molo.domain.usecase.user

import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.map
import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import de.convelop.forge.usecase.FlowOutcomeUseCase
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.repository.AuthRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.error.MoloError

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : FlowOutcomeUseCase<LoginUseCase.Params, Unit, MoloError>() {

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.execute(
        params: Params
    ) {
        val response = authRepository.authenticateUser(
            email = params.email,
            password = params.password
        ).onSuccess { authResponse ->
            sessionRepository.setBearer(authResponse.accessToken)
            sessionRepository.setUserId(authResponse.userId)
            sessionRepository.triggerProfileReload()
        }.flatMap {
            userRepository.fetchSelfUser(it.accessToken)
        }.onSuccess {
            sessionRepository.setSelfUserOrganisationId(it.organizationId)
        }.map { }

        emitFrom(response) {
            MoloError.Unauthenticated
        }
    }
}