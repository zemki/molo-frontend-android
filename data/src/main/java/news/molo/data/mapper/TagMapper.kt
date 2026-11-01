package news.molo.data.mapper

import news.molo.data.database.entity.TagEntity
import news.molo.data.network.response.TagCategoryResponse
import news.molo.data.network.response.TagResponse
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory

fun TagCategoryResponse.toTagCategory(): TagCategory = TagCategory(
    id = this.id,
    name = this.name,
    order = this.order,
    tags = this.tags.map { it.toTag() }
)

fun TagResponse.toTag(): Tag = Tag(
    id = this.id,
    name = this.name,
    isSelected = this.selected
)

fun Tag.toEntity(): TagEntity = TagEntity(
    id = this.id,
    name = this.name,
    selected = this.isSelected
)

fun TagEntity.toTag(): Tag = Tag(
    id = this.id,
    name = this.name,
    isSelected = this.selected
)