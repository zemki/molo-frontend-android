package news.molo.domain.usecase.organization

import de.convelop.forge.outcome.Outcome
import kotlinx.coroutines.flow.FlowCollector
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.OrganizationRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.usecase.DeviceIDAwareOutcomeUseCase
import news.molo.domain.usecase.error.MoloError

class EnableOrganizationUseCase(
    private val organizationRepository: OrganizationRepository,
    deviceIdProvider: DeviceIdProvider
) : DeviceIDAwareOutcomeUseCase<Int, Unit>(deviceIdProvider) {

    override suspend fun FlowCollector<Outcome<Unit, MoloError>>.executeWithDeviceId(
        deviceId: String,
        param: Int
    ) {
        val response = organizationRepository.enableOrganization(deviceId = deviceId, id = param)
        emitFrom(response) {
            MoloError.OrganizationLoadingFailed
        }
    }
}