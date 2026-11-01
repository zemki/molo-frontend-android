package news.molo.domain.usecase.onboarding

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import news.molo.domain.repository.SessionRepository

class ObserveOnboardingCompletionUseCase(
    private val sessionRepository: SessionRepository
) : FlowUseCase<Unit, Boolean>() {

    override fun execute(params: Unit): Flow<Boolean> = sessionRepository.getIsOnboardingCompletedAsFlow()
}