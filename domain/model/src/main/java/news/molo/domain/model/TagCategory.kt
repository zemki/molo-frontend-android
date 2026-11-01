package news.molo.domain.model

data class TagCategory(
    val id: Int,
    val name: String,
    val order: Int,
    val tags: List<Tag>
) {

    companion object {
        val mock = TagCategory(
            id = 1,
            name = "Category Name",
            order = 1,
            tags = listOf(Tag.mock)
        )
    }
}