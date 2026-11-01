package news.molo.data.repository

import de.convelop.forge.datastore.DataStore
import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import news.molo.data.datastore.MoloDataEntry
import news.molo.data.mapper.toAuthResponse
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.request.ChangePasswordRequest
import news.molo.data.network.request.LoginRequest
import news.molo.data.network.request.RequestPasswordResetRequest
import news.molo.data.network.response.LoginResponse
import news.molo.domain.model.AuthResponse
import news.molo.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val dataStore: DataStore
) : AuthRepository {

    override suspend fun authenticateUser(
        email: String,
        password: String
    ): ApiResponse<AuthResponse> = httpClient.execute<LoginResponse, AuthResponse>(
        mapper = { response ->
            response.toAuthResponse()
        },
        requestBuilder = {
            post(Endpoint.Token)
            body(LoginRequest(username = email, password = password))
        }
    )

    override suspend fun isUserLoggedIn(): Boolean =
        !dataStore.get(MoloDataEntry.AuthToken).isNullOrBlank()

    override suspend fun isTokenValid(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun requestPasswordReset(
        email: String
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User.ResetPasswordRequest)
            body(RequestPasswordResetRequest(email))
        }
    )

    override suspend fun resetPassword(
        token: String,
        password: String
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User.ChangePassword)
            header(HeaderValue.Bearer(token))
            body(ChangePasswordRequest(password))
        }
    )
}