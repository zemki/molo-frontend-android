package news.molo.domain.usecase.user

import de.convelop.forge.usecase.UseCase
import news.molo.domain.repository.SessionRepository

class LogoutUseCase(
    private val sessionRepository: SessionRepository
) : UseCase<Unit, Unit, String>() {

    override suspend fun UseCaseScope<String>.execute(params: Unit) {
        sessionRepository.deleteAuthToken()
    }

    override fun mapError(t: Throwable): String {
        return t.message ?: ""
    }
}