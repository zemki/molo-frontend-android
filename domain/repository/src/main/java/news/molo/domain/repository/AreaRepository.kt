package news.molo.domain.repository

import de.convelop.forge.network.response.ApiResponse
import news.molo.domain.model.Area

interface AreaRepository {

    suspend fun fetchAreas(
        deviceId: String,
        query: String = "",
        longitude: String? = null,
        latitude: String? = null
    ): ApiResponse<List<Area>>

    suspend fun fetchAreasByRadius(
        deviceId: String,
        latitude: String,
        longitude: String,
        radius: Int
    ): ApiResponse<List<Area>>
}