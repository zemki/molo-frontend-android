package news.molo.domain.usecase.user

import de.convelop.forge.usecase.UseCase
import news.molo.domain.repository.SessionRepository

class SetBearerUseCase(
    private val sessionRepository: SessionRepository
) : UseCase<String, Unit, String>() {

    override suspend fun UseCaseScope<String>.execute(params: String) {
        sessionRepository.setBearer(params)
    }

    override fun mapError(t: Throwable): String {
        return t.message ?: ""
    }
}