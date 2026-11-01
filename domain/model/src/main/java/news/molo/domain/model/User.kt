package news.molo.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val imageUrl: String? = null,
    val imageSource: String? = null,
    val zip: String,
    val phone: String,
    val mail: String,
    val description: String,
    val homepage: String,
    val area: Area,
    val organizationId: Int
) {
    companion object {
        val mock = User(
            id = 1,
            firstName = "Test",
            lastName = "User",
            displayName = "Dev",
            imageUrl = null,
            imageSource = "abc",
            zip = "282359",
            phone = "012345678",
            mail = "test@example.com",
            description = "Thats just a example",
            homepage = "https://convelop.de",
            area = Area(),
            organizationId = 1
        )
    }
}
