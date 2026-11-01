package news.molo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_article_ids")
data class BookmarkedArticleId(
    @PrimaryKey
    val id: Int = 0,
)
