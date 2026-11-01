package news.molo.domain.model

data class UserRegistration(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val displayName: String = "",
)