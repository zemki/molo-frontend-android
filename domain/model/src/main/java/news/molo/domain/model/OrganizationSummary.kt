package news.molo.domain.model

data class OrganizationSummary(
    val allTags: Int,
    val byTag: Int,
    val deselected: Int,
    val name: String,
    val push: Boolean,
    val category: Category,
    val organizations: List<Organization>
) {

    companion object {
        val mock = OrganizationSummary(
            allTags = 10,
            byTag = 10,
            deselected = 10,
            name = "Example",
            push = true,
            category = Category.OFFICIAL,
            organizations = emptyList()
        )
    }
}