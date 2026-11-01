package news.molo.feature.location.presentation

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import news.molo.domain.model.Area
import news.molo.domain.model.SelectedLocation

data class LocationState(
    val query: String = "",
    val radiusInKm: Int = 10,
    val areasWithDistance: List<Pair<Area, Double>> = emptyList(),
    val filteredAreas: List<Area> = emptyList(),
    val location: LatLng? = null,
    val isLoading: Boolean = false,
    val isMapRendered: Boolean = false,
    val locationSelection: SelectedLocation? = null,
    val cameraBounds: LatLngBounds? = null
) {

    sealed interface Intent {
        data object MarkMapAsLoaded : Intent
        data class SetLocation(val location: LatLng) : Intent
        data class SetRadius(val radiusInKm: Int) : Intent
        data class SetQuery(val query: String) : Intent
        data object SubmitLocation : Intent
    }
}