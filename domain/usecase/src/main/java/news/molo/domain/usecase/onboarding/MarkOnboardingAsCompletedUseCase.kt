package news.molo.domain.usecase.onboarding

import de.convelop.forge.usecase.UseCase
import news.molo.domain.repository.SessionRepository

class MarkOnboardingAsCompletedUseCase(
    private val sessionRepository: SessionRepository
) : UseCase<Unit, Unit, String>() {

    override suspend fun UseCaseScope<String>.execute(params: Unit) {
        sessionRepository.setOnboardingCompleted(true)
    }

    override fun mapError(t: Throwable): String {
        return t.message ?: ""
    }
}