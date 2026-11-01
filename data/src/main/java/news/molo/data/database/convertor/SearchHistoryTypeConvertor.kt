package news.molo.data.database.convertor

import androidx.room.TypeConverter
import news.molo.domain.model.SearchHistoryType

class SearchHistoryTypeConverter {

    @TypeConverter
    fun fromSearchHistoryType(type: SearchHistoryType): String {
        return type.name
    }

    @TypeConverter
    fun toSearchHistoryType(value: String): SearchHistoryType {
        return try {
            SearchHistoryType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            SearchHistoryType.Text
        }
    }
}