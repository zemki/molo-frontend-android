package news.molo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import news.molo.data.database.dao.SearchHistoryDao
import news.molo.data.mapper.toDomain
import news.molo.data.mapper.toEntity
import news.molo.domain.model.SearchHistoryEntry
import news.molo.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {

    override fun observeSearchHistory(query: String): Flow<List<SearchHistoryEntry>> {
        return searchHistoryDao.getSearchHistory(query).map {
            it.map { it.toDomain() }
        }
    }

    override suspend fun insertSearchEntry(entry: SearchHistoryEntry) {
        searchHistoryDao.addSearchEntry(entry.toEntity())
    }

    override suspend fun deleteSearchEntry(id: Int) {
        searchHistoryDao.deleteById(id)
    }

    override suspend fun deleteWholeSearchHistory() {
        searchHistoryDao.deleteAll()
    }
}