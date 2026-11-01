package news.molo.domain.model

data class AuthResponse(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String
)