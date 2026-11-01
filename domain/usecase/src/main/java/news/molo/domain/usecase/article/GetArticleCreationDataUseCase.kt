package news.molo.domain.usecase.article

import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.map
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.TagCategory
import news.molo.domain.model.User
import news.molo.domain.model.toggleSelection
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetArticleCreationDataUseCase(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val tagRepository: TagRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Unit, Pair<User, List<TagCategory>>>(deviceIdProvider) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun FlowCollector<Outcome<Pair<User, List<TagCategory>>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {

        val bearer = sessionRepository.getAuthToken().getOrNull() ?: run {
            emitFailure(MoloError.UserNotLoggedIn)
            return
        }

        val response = userRepository.fetchSelfUser(bearer = bearer).flatMap { user ->
            tagRepository.fetchArticleTagCategories(deviceId = deviceId)
                .map { tags -> user to tags.toggleSelection(false) }
        }

        emitFrom(response) { MoloError.UserNotLoggedIn }
    }
}