package news.molo.domain.model

data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val areaId: Int,
    val distance: Int
)
