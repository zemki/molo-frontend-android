package news.molo.domain.usecase.event

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import news.molo.domain.repository.EventRepository

class ObserveEventBookmarksUseCase(
    private val eventRepository: EventRepository
) : FlowUseCase<Unit, List<Int>>() {

    override fun execute(params: Unit): Flow<List<Int>> = eventRepository.observeBookmarkSelection()
}