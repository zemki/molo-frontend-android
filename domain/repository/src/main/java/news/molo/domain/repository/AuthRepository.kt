package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.AuthResponse

interface AuthRepository {
    suspend fun authenticateUser(email: String, password: String): ApiResponse<AuthResponse>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isTokenValid(token: String): Boolean
    suspend fun requestPasswordReset(email: String): ApiResponse<Unit>
    suspend fun resetPassword(token: String, password: String): ApiResponse<Unit>
}