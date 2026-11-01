package news.molo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_event_ids")
data class BookmarkedEventId(
    @PrimaryKey
    val id: Int = 0,
)