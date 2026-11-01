package news.molo.domain.usecase.user

import de.convelop.forge.outcome.Outcome
import de.convelop.forge.usecase.FlowOutcomeUseCase
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.repository.AuthRepository
import news.molo.domain.usecase.error.MoloError

class RequestPasswordResetUseCase(
    private val authRepository: AuthRepository,
) : FlowOutcomeUseCase<String, Unit, MoloError>() {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.execute(
        params: String
    ) {
        emitFrom(authRepository.requestPasswordReset(params)) {
            MoloError.PasswordResetRequestFailed
        }
    }
}