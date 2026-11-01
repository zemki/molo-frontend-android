package news.molo.data.mapper

import news.molo.data.network.response.LoginResponse
import news.molo.domain.model.AuthResponse

fun LoginResponse.toAuthResponse(): AuthResponse = AuthResponse(
    userId = this.userId,
    accessToken = this.access,
    refreshToken = this.refresh
)