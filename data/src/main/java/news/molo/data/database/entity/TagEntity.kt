package news.molo.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val selected: Boolean = false
)