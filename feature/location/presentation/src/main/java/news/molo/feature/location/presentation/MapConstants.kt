package news.molo.feature.location.presentation

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object MapConstants {
    val GERMANY_BOUNDS = LatLngBounds(
        LatLng(47.2, 5.8),
        LatLng(55.1, 15.1)
    )
    const val MAX_RADIUS = 200
}