package news.molo.data.mapper

import news.molo.data.network.response.AreaResponse
import news.molo.domain.model.Area

fun AreaResponse.toArea(): Area = Area(
    id = this.id,
    name = this.name,
    locationLatitude = this.latitude?.toDoubleOrNull() ?: 0.0,
    locationLongitude = this.longitude?.toDoubleOrNull()?: 0.0,
    isSelected = false
)