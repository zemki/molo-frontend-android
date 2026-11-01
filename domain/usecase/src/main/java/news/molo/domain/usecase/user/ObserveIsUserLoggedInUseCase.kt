package news.molo.domain.usecase.user

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import news.molo.domain.repository.SessionRepository

class ObserveIsUserLoggedInUseCase(
    private val sessionRepository: SessionRepository
) : FlowUseCase<Unit, Boolean>() {

    override fun execute(params: Unit): Flow<Boolean> = sessionRepository
        .getAuthTokenAsFlow()
        .map { it.isNotBlank() }
}