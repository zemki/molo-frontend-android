package news.molo.domain.model

data class Tag(
    val id: Int,
    val name: String = "",
    val isSelected: Boolean = false,
) {
    companion object {
        val mock = Tag(
            id = 1,
            name = "Example Tag",
            isSelected = true,
        )
    }
}

fun List<TagCategory>.withSelectedTags(isSelected: (Tag) -> Boolean): List<TagCategory> {
    return this.map { category ->
        category.copy(
            tags = category.tags.map { tag ->
                tag.copy(isSelected = isSelected(tag))
            }
        )
    }
}

fun List<TagCategory>.toggleSelection(isSelected: Boolean): List<TagCategory> {
    return this.map { category ->
        category.copy(
            tags = category.tags.map { tag ->
                tag.copy(isSelected = isSelected)
            }
        )
    }
}