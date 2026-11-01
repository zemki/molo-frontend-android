package news.molo.domain.repository

import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.SearchHistoryEntry

interface SearchHistoryRepository {

    fun observeSearchHistory(query: String): Flow<List<SearchHistoryEntry>>
    suspend fun insertSearchEntry(entry: SearchHistoryEntry)
    suspend fun deleteSearchEntry(id: Int)
    suspend fun deleteWholeSearchHistory()

}