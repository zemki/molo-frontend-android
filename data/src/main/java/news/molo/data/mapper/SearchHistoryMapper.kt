package news.molo.data.mapper

import news.molo.data.database.entity.SearchHistoryEntryEntity
import news.molo.domain.model.SearchHistoryEntry

fun SearchHistoryEntryEntity.toDomain(): SearchHistoryEntry = SearchHistoryEntry(
    id = id,
    query = query,
    type = type
)

fun SearchHistoryEntry.toEntity(): SearchHistoryEntryEntity = SearchHistoryEntryEntity(
    id = id,
    query = query,
    type = type
)