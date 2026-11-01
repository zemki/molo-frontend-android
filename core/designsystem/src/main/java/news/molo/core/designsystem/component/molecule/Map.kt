package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.map.MapStyleManager
import news.molo.core.util.preview.ComponentPreview

@Composable
fun Map(
    modifier: Modifier = Modifier,
    lat: Double,
    lng: Double,
    onMapClick: () -> Unit,
) {

    val position = LatLng(lat, lng)

    var isMapReady by remember { mutableStateOf(false) }
    var isStyleReady by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition.fromLatLngZoom(position, 15f)
    }

    val markerState = remember(position) {
        MarkerState(position = position)
    }
    val isDarkTheme = MaterialTheme.isDarkTheme
    var mapStyle by remember { mutableStateOf<MapStyleOptions?>(null) }

    LaunchedEffect(isDarkTheme) {
        MapStyleManager.getStyle(isDarkTheme)
        mapStyle = MapStyleManager.getStyle(isDarkTheme)
        isStyleReady = true
    }

    val mapProperties = remember(mapStyle) {
        MapProperties(mapStyleOptions = mapStyle)
    }

    Box(
        Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        GoogleMap(
            modifier = Modifier
                .clip(RoundedCornerShape(CornerRadius.Medium))
                .alpha(if (isMapReady && isStyleReady) 1f else 0f),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
                scrollGesturesEnabled = false,
                tiltGesturesEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false,
            ),
            onMapLoaded = {
                isMapReady = true
            },
            onMapClick = { onMapClick() }
        ) {
            Marker(state = markerState)
        }

        if(!isMapReady || !isStyleReady) {
            CoveringProgressIndicator()
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        Map(
            modifier = Modifier.size(300.dp),
            lat = 50.0,
            lng = 8.0,
            onMapClick = {}
        )
    }
}
