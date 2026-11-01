package news.molo.domain.usecase.user

import de.convelop.forge.network.response.ApiResponse
import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.onSuccess
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.User
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.provider.FileProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.AuthorizedOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class UpdateUserProfileUseCase(
    private val userRepository: UserRepository,
    deviceIdProvider: DeviceIdProvider,
    private val sessionRepository: SessionRepository,
    private val fileProvider: FileProvider
) : AuthorizedOutcomeUseCase<UpdateUserProfileUseCase.Params, Unit>(
    sessionRepository = sessionRepository,
    deviceIdProvider = deviceIdProvider
) {

    data class Params(
        val originalUser: User,
        val user: User
    )

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithBearer(
        bearer: String,
        deviceId: String,
        param: Params
    ) {
        val response = uploadImageIfChanged(bearer, param)
            .flatMap {
                userRepository.updateUser(bearer = bearer, user = param.user)
            }.onSuccess {
                sessionRepository.triggerProfileReload()
            }

        emitFrom(response) {
            MoloError.ProfileUpdateFailed
        }
    }

    private suspend fun uploadImageIfChanged(
        bearer: String,
        param: Params
    ): ApiResponse<Unit> {
        val newImageUrl = param.user.imageUrl
        val hasChanged = newImageUrl != param.originalUser.imageUrl && !newImageUrl.isNullOrEmpty()

        if (!hasChanged) {
            return ApiResponse.Success(Unit)
        }

        val file = fileProvider.resolveFile(newImageUrl).getOrNull()

        return if (file != null) {
            userRepository.uploadProfilePictureForSelfUser(
                bearer = bearer,
                file = file,
                source = param.user.imageSource.orEmpty()
            )
        } else {
            ApiResponse.Error.SerializationError(reason = "File not found")
        }
    }
}