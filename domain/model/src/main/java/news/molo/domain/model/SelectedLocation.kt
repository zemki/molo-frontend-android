package news.molo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SelectedLocation(
    val town: String,
    val street: String,
    val zip: String,
    val latitude: Double,
    val longitude: Double
): java.io.Serializable
