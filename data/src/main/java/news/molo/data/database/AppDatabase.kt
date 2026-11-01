package news.molo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import news.molo.data.database.convertor.SearchHistoryTypeConverter
import news.molo.data.database.dao.BookmarkedArticleIdDao
import news.molo.data.database.dao.BookmarkedEventDao
import news.molo.data.database.dao.SearchHistoryDao
import news.molo.data.database.dao.TagDao
import news.molo.data.database.entity.BookmarkedArticleId
import news.molo.data.database.entity.BookmarkedEventId
import news.molo.data.database.entity.SearchHistoryEntryEntity
import news.molo.data.database.entity.TagEntity

@Database(
    entities = [BookmarkedArticleId::class, BookmarkedEventId::class, TagEntity::class, SearchHistoryEntryEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(SearchHistoryTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarkedArticleDao(): BookmarkedArticleIdDao
    abstract fun bookmarkedEventsDao(): BookmarkedEventDao
    abstract fun tagDao(): TagDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        fun create(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}