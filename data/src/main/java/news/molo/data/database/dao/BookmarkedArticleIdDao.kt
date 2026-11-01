package news.molo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import news.molo.data.database.entity.BookmarkedArticleId

@Dao
interface BookmarkedArticleIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedArticleId(id: BookmarkedArticleId)

    @Query("SELECT * FROM bookmarked_article_ids")
    fun getAllBookmarkedArticle(): Flow<List<BookmarkedArticleId>>

    @Query("DELETE FROM bookmarked_article_ids WHERE id = :id")
    suspend fun deleteBookmarkedArticleId(id: Int)
}