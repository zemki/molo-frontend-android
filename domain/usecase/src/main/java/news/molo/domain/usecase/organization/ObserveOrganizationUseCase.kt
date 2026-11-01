package news.molo.domain.usecase.organization

import de.convelop.forge.network.response.map
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import news.molo.domain.model.Organization
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.OrganizationRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class ObserveOrganizationUseCase(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Organization>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Organization, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val selfUserOrganisationId = sessionRepository.getSelfUserOrganisationId().getOrNull()

        sessionRepository.getProfileReloadTriggerAsFlow()
            .map {
                organizationRepository.fetchOrganizationById(deviceId, param)
                    .map { organization ->
                        organization.copy(isOwnedBySelfUser = organization.id == selfUserOrganisationId)
                    }
            }.collect {
                emitFrom(it) { MoloError.OrganizationLoadingFailed }
            }
    }
}