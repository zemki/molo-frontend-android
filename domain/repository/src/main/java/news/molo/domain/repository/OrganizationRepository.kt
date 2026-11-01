package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import news.molo.domain.model.Organization
import news.molo.domain.model.OrganizationSummary

interface OrganizationRepository {

    suspend fun fetchOrganizationSummaries(deviceId: String): ApiResponse<List<OrganizationSummary>>
    suspend fun fetchOrganizations(
        deviceId: String
    ): ApiResponse<List<Organization>>

    suspend fun fetchOrganizationById(
        deviceId: String,
        organizationId: Int
    ): ApiResponse<Organization>

    suspend fun enableOrganization(
        deviceId: String,
        id: Int
    ): ApiResponse<Unit>

    suspend fun disableOrganization(
        deviceId: String,
        id: Int
    ): ApiResponse<Unit>
}