package news.molo.domain.usecase.search

import de.convelop.forge.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.SearchHistoryEntry
import news.molo.domain.repository.SearchHistoryRepository

class ObserveSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) : FlowUseCase<String, List<SearchHistoryEntry>>() {

    override fun execute(params: String): Flow<List<SearchHistoryEntry>> {
        return searchHistoryRepository.observeSearchHistory(params)
    }
}