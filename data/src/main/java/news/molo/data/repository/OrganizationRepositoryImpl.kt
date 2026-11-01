package news.molo.data.repository

import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import news.molo.data.mapper.toOrganization
import news.molo.data.mapper.toOrganizationSummary
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.request.OrganizationRequest
import news.molo.data.network.response.OrganizationResponse
import news.molo.data.network.response.OrganizationSummaryListResponse
import news.molo.domain.model.Organization
import news.molo.domain.model.OrganizationSummary
import news.molo.domain.repository.OrganizationRepository

class OrganizationRepositoryImpl(
    private val httpClient: HttpClient
) : OrganizationRepository {

    override suspend fun fetchOrganizationSummaries(
        deviceId: String
    ): ApiResponse<List<OrganizationSummary>> =
        httpClient.execute<OrganizationSummaryListResponse, List<OrganizationSummary>>(
            mapper = { response ->
                response.overview.map { it.toOrganizationSummary() }
            },
            requestBuilder = {
                get(Endpoint.User.Summary)
                header(HeaderValue.DeviceID(deviceId))
            }
        )

    override suspend fun fetchOrganizations(
        deviceId: String,
    ): ApiResponse<List<Organization>> = httpClient.execute<List<OrganizationResponse>, List<Organization>>(
        mapper = { response ->
            response.map { it.toOrganization() }
        },
        requestBuilder = {
            get(Endpoint.Organization)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                put("type", "all")
            }
        }
    )

    override suspend fun fetchOrganizationById(
        deviceId: String,
        organizationId: Int
    ): ApiResponse<Organization> = httpClient.execute<OrganizationResponse, Organization>(
        mapper = { response ->
            response.toOrganization()
        },
        requestBuilder = {
            get(Endpoint.Organization.path + "/$organizationId")
            header(HeaderValue.DeviceID(deviceId))
        }
    )

    override suspend fun enableOrganization(
        deviceId: String,
        id: Int,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            post(Endpoint.User.AddOrganization)
            header(HeaderValue.DeviceID(deviceId))
            body(OrganizationRequest(id))
        }
    )

    override suspend fun disableOrganization(
        deviceId: String,
        id: Int,
    ): ApiResponse<Unit> = httpClient.execute<Unit, Unit>(
        mapper = { },
        requestBuilder = {
            post(Endpoint.User.RemoveOrganization)
            header(HeaderValue.DeviceID(deviceId))
            body(OrganizationRequest(id))
        }
    )
}