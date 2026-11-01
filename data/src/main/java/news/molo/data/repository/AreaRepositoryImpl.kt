package news.molo.data.repository

import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.client.execute
import de.convelop.forge.network.response.ApiResponse
import news.molo.data.mapper.toArea
import news.molo.data.network.endpoint.Endpoint
import news.molo.data.network.endpoint.HeaderValue
import news.molo.data.network.response.AreaListResponse
import news.molo.domain.model.Area
import news.molo.domain.repository.AreaRepository

class AreaRepositoryImpl(
    private val httpClient: HttpClient
) : AreaRepository {

    override suspend fun fetchAreas(
        deviceId: String,
        query: String,
        longitude: String?,
        latitude: String?
    ): ApiResponse<List<Area>> = httpClient.execute<AreaListResponse, List<Area>>(
        mapper = { response ->
            response.results.map { it.toArea() }
        },
        requestBuilder = {
            get(Endpoint.Area)
            header(HeaderValue.DeviceID(deviceId))

            parameters {
                if (query.isNotBlank()) {
                    put("area", query)
                }
                if (!latitude.isNullOrBlank()) {
                    put("latitude", latitude)
                }
                if (!longitude.isNullOrBlank()) {
                    put("longitude", longitude)
                }
            }

        }
    )

    override suspend fun fetchAreasByRadius(
        deviceId: String,
        latitude: String,
        longitude: String,
        radius: Int
    ): ApiResponse<List<Area>> = httpClient.execute<AreaListResponse, List<Area>>(
        mapper = { response ->
            response.results.map { it.toArea() }
        },
        requestBuilder = {
            get(Endpoint.Area.ByDistance)
            header(HeaderValue.DeviceID(deviceId))
            parameters {
                if (latitude.isNotBlank()) {
                    put("latitude", latitude)
                }
                if (longitude.isNotBlank()) {
                    put("longitude", longitude)
                }

                put("distance", radius.toString())
            }
        }
    )
}
