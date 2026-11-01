package news.molo.domain.usecase.user

import de.convelop.forge.outcome.Outcome
import de.convelop.forge.usecase.FlowOutcomeUseCase
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.repository.AuthRepository
import news.molo.domain.usecase.error.MoloError

class ResetPasswordUseCase(
    private val authRepository: AuthRepository,
) : FlowOutcomeUseCase<ResetPasswordUseCase.Params, Unit, MoloError>() {

    data class Params(
        val token: String,
        val password: String
    )

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.execute(
        params: Params
    ) {

        emitFrom(authRepository.resetPassword(token = params.token, password = params.password)) {
            MoloError.PasswordResetFailed
        }
    }
}