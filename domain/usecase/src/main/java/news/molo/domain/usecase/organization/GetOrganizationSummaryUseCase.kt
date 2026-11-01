package news.molo.domain.usecase.organization

import de.convelop.forge.network.response.flatMap
import de.convelop.forge.network.response.map
import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.model.OrganizationSummary
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.OrganizationRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class GetOrganizationSummaryUseCase(
    private val organizationRepository: OrganizationRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Unit, List<OrganizationSummary>>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<List<OrganizationSummary>, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Unit
    ) {
        val response = organizationRepository.fetchOrganizations(deviceId)
            .flatMap { organizations ->
                organizationRepository.fetchOrganizationSummaries(deviceId)
                    .map { summaries ->
                        summaries.map { summary ->
                            summary.copy(organizations = organizations.filter { it.category == summary.category })
                        }
                    }
            }

        emitFrom(response) {
            MoloError.OrganizationLoadingFailed
        }
    }
}