package news.molo.domain.usecase.area

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import news.molo.domain.repository.SessionRepository

class ObserveAreaAndRadiusSelectionUseCase(
    private val sessionRepository: SessionRepository
) : FlowUseCase<Unit, String>() {

    override fun execute(params: Unit): Flow<String> = sessionRepository
        .getSelectedLocationWithRadiusAsFlow()
        .map { createAreaTitle(it.area?.name, it.radius) }

    private fun createAreaTitle(areaName: String?, radius: Int?): String {
        if (areaName.isNullOrBlank()) return ""
        if (radius == null) return ""
        return "$areaName + $radius km"
    }
}

