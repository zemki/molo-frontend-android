package news.molo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Area(
    val id: Int = 0,
    val name: String = "",
    val locationLatitude: Double = 0.0,
    val locationLongitude: Double = 0.0,
    val isSelected: Boolean = false
): java.io.Serializable {

    companion object {
        val mock = Area(
            id = 1,
            name = "Bremen",
            locationLatitude = 0.0,
            locationLongitude = 0.0,
            isSelected = false
        )
    }
}