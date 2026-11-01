package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import news.molo.domain.model.User
import news.molo.domain.model.UserLocation
import news.molo.domain.model.UserRegistration
import java.io.File

interface UserRepository {

    suspend fun fetchSelfUser(bearer: String): ApiResponse<User>
    suspend fun submitLocation(deviceId: String, location: UserLocation): ApiResponse<Unit>
    suspend fun registerUser(deviceId: String, user: UserRegistration): ApiResponse<Unit>
    suspend fun deleteSelfUser(bearer: String): ApiResponse<Unit>
    suspend fun updateUser(bearer: String, user: User): ApiResponse<Unit>
    suspend fun uploadProfilePictureForSelfUser(
        bearer: String,
        file: File,
        source: String
    ): ApiResponse<Unit>

    suspend fun submitPushToken(deviceId: String, token: String): ApiResponse<Unit>

}