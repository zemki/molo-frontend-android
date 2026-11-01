package news.molo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import news.molo.data.database.entity.BookmarkedEventId

@Dao
interface BookmarkedEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedEventId(id: BookmarkedEventId)

    @Query("SELECT * FROM bookmarked_event_ids")
    fun getAllBookmarkedEvents(): Flow<List<BookmarkedEventId>>

    @Query("DELETE FROM bookmarked_event_ids WHERE id = :id")
    suspend fun deleteBookmarkedEventId(id: Int)
}