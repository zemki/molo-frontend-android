package news.molo.domain.usecase.tag

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.Tag
import news.molo.domain.repository.TagRepository

class ObserveTagSelectionUseCase(
    private val tagRepository: TagRepository
) : FlowUseCase<Unit, List<Tag>>() {

    override fun execute(params: Unit): Flow<List<Tag>> = tagRepository.getAllTagsFromCache()
}