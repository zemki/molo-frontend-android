package news.molo.domain.model

data class Organization(
    val id: Int,
    val name: String = "",
    val imageURL: String = "",
    val address: String = "",
    val description: String = "",
    val homepage: String = "",
    val isSelected: Boolean = false,
    val title: String = "",
    val category: Category,
    val isOwnedBySelfUser: Boolean
) {
    companion object {
        val mockOrganization = Organization(
            id = 1,
            name = "Convelop Convelop Convelop",
            imageURL = "https://convelop.de/wp-content/uploads/2018/10/Convelop-Logo@2x.png",
            address = "Example address",
            description = "Just a description idk",
            homepage = "https://convelop.de",
            isSelected = true,
            title = "Convelop",
            category = Category.COMMUNITY,
            isOwnedBySelfUser = true
        )
    }
}