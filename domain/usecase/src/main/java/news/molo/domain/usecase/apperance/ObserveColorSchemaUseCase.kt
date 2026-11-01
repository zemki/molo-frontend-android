package news.molo.domain.usecase.apperance

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.ColorSchemaPreference
import news.molo.domain.repository.SessionRepository

class ObserveColorSchemaUseCase(
    private val sessionRepository: SessionRepository
) : FlowUseCase<Unit, ColorSchemaPreference>() {

    override fun execute(params: Unit): Flow<ColorSchemaPreference> = sessionRepository.getColorSchemaAsFlow()
}