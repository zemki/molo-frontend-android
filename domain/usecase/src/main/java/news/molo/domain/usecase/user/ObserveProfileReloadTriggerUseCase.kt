package news.molo.domain.usecase.user

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import news.molo.domain.repository.SessionRepository

class ObserveProfileReloadTriggerUseCase(
    private val sessionRepository: SessionRepository
) : FlowUseCase<Unit, Unit>() {
    override fun execute(params: Unit): Flow<Unit> {
        return sessionRepository.getProfileReloadTriggerAsFlow().map { }
    }
}