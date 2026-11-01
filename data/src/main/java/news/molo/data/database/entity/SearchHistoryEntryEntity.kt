package news.molo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import news.molo.domain.model.SearchHistoryType

@Entity(tableName = "search_history_entry")
data class SearchHistoryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val query: String = "",
    val type: SearchHistoryType
)