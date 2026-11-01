package news.molo.data.repository

import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import kotlinx.serialization.json.JsonObject
import news.molo.data.mapper.toUser
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.request.PushTokenRequest
import news.molo.data.network.request.RegistrationRequest
import news.molo.data.network.request.UpdateUserRequest
import news.molo.data.network.response.UserResponse
import news.molo.domain.model.User
import news.molo.domain.model.UserLocation
import news.molo.domain.model.UserRegistration
import news.molo.domain.repository.UserRepository
import java.io.File

class UserRepositoryImpl(
    private val httpClient: HttpClient,
) : UserRepository {

    override suspend fun fetchSelfUser(
        bearer: String
    ): ApiResponse<User> = httpClient.execute<UserResponse, User>(
        mapper = { it.toUser() },
        requestBuilder = {
            get(Endpoint.User.Details)
            header(HeaderValue.Bearer(bearer))
        }
    )

    override suspend fun submitLocation(
        deviceId: String,
        location: UserLocation,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User.SetAreas)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                putAll(
                    mapOf(
                        "longitude" to location.longitude.toString(),
                        "latitude" to location.latitude.toString(),
                        "distance" to location.distance.toString(),
                    )
                )
            }
            body(JsonObject(emptyMap()))
        }
    )

    override suspend fun registerUser(
        deviceId: String,
        user: UserRegistration,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User)
            header(HeaderValue.DeviceID(deviceId))
            body(
                RegistrationRequest(
                    displayName = user.displayName,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    password = user.password,
                    username = user.displayName
                )
            )
        }
    )

    override suspend fun deleteSelfUser(
        bearer: String
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            delete(Endpoint.User.Delete)
            header(HeaderValue.Bearer(bearer))
        }
    )

    override suspend fun updateUser(
        bearer: String,
        user: User,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            put(Endpoint.User.UpdateUser)
            header(HeaderValue.Bearer(bearer))
            body(
                UpdateUserRequest(
                    area = user.area.id,
                    displayName = user.displayName,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    phone = user.phone,
                    username = user.displayName,
                    homepage = user.homepage,
                    description = user.description,
                )
            )
        }
    )

    override suspend fun uploadProfilePictureForSelfUser(
        bearer: String,
        file: File,
        source: String,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User.PictureUpload)
            header(HeaderValue.Bearer(bearer))
            multipartBody {
                addFile(
                    name = "picture",
                    filename = file.name,
                    content = file.readBytes(),
                    contentType = "octet-stream"
                )
                add("image_source", source)
            }
        }
    )

    override suspend fun submitPushToken(
        deviceId: String,
        token: String
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = {},
        requestBuilder = {
            post(Endpoint.User.Push)
            header(HeaderValue.DeviceID(deviceId))
            body(
                PushTokenRequest(
                    firebaseToken = token
                )
            )
        }
    )
}