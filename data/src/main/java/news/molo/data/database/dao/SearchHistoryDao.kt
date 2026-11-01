package news.molo.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import news.molo.data.database.entity.SearchHistoryEntryEntity

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history_entry WHERE `query` LIKE '%' || :query || '%' ORDER BY id DESC")
    fun getSearchHistory(query: String): Flow<List<SearchHistoryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRaw(entry: SearchHistoryEntryEntity)

    @Query("DELETE FROM search_history_entry WHERE query = :query")
    suspend fun deleteByQuery(query: String)

    @Query(
        """
        DELETE FROM search_history_entry 
        WHERE id NOT IN (
            SELECT id FROM search_history_entry 
            ORDER BY id DESC 
            LIMIT 100
        )
    """
    )
    suspend fun trimHistory()

    @Transaction
    suspend fun addSearchEntry(entry: SearchHistoryEntryEntity) {
        deleteByQuery(entry.query)
        insertRaw(entry)
        trimHistory()
    }

    @Query("DELETE FROM search_history_entry WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM search_history_entry")
    suspend fun deleteAll()
}