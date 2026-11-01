package news.molo.feature.location.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.LocationSearchField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.map.MapStyleManager
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Area
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.SelectedLocation
import news.molo.feature.location.ui.component.LocationSelectionCard
import news.molo.feature.location.ui.component.RadiusCard

@Composable
internal fun LocationPage(
    cameraPositionState: CameraPositionState,
    location: LatLng?,
    radiusInKm: Int,
    query: String,
    areas: List<Area>,
    configurationType: ConfigurationType,
    selectedLocation: SelectedLocation?,
    isMapRendered: Boolean,
    isLoading: Boolean,
    onMapReady: () -> Unit,
    onQueryChange: (String) -> Unit,
    onLocationChange: (LatLng) -> Unit,
    onRadiusChange: (Int) -> Unit,
    onRequestLocationClick: () -> Unit,
    onLocationCardClick: () -> Unit
) {

    if (isLoading) {
        CoveringProgressIndicator()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LocationMap(
            location = location,
            radiusInKm = radiusInKm,
            areas = areas,
            locationSelection = selectedLocation,
            isMapRendered = isMapRendered,
            configurationType = configurationType,
            onMapReady = onMapReady,
            onMapClick = onLocationChange,
            cameraPositionState = cameraPositionState
        )

        MapOverlay(
            isMapRendered = isMapRendered,
            query = query,
            onQueryChange = onQueryChange,
            onRequestLocationClick = onRequestLocationClick,
            location = location,
            radiusInKm = radiusInKm,
            configurationType = configurationType,
            selectedLocation = selectedLocation,
            onRadiusChange = onRadiusChange,
            onLocationCardClick = onLocationCardClick
        )
    }
}

@Composable
private fun LocationMap(
    cameraPositionState: CameraPositionState,
    location: LatLng?,
    radiusInKm: Int,
    areas: List<Area>,
    locationSelection: SelectedLocation?,
    isMapRendered: Boolean,
    configurationType: ConfigurationType,
    onMapReady: () -> Unit,
    onMapClick: (LatLng) -> Unit
) {

    val isDarkTheme = MaterialTheme.isDarkTheme

    val mapProperties = remember(isDarkTheme) {
        MapProperties(mapStyleOptions = MapStyleManager.getStyle(isDarkTheme))
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .alpha(if (isMapRendered) 1f else 0f),
        cameraPositionState = cameraPositionState,
        onMapLoaded = onMapReady,
        properties = mapProperties,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            zoomGesturesEnabled = true,
            scrollGesturesEnabled = true,
        ),
        onMapClick = onMapClick
    ) {
        MapContent(
            configurationType = configurationType,
            location = location,
            radiusInKm = radiusInKm,
            areas = areas,
            locationSelection = locationSelection
        )
    }
}

@Composable
private fun MapOverlay(
    isMapRendered: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onRequestLocationClick: () -> Unit,
    location: LatLng?,
    radiusInKm: Int,
    configurationType: ConfigurationType,
    selectedLocation: SelectedLocation?,
    onRadiusChange: (Int) -> Unit,
    onLocationCardClick: () -> Unit
) {
    PageTemplate(modifier = Modifier.systemBarsPadding(), clearFocusOnTouch = false) {
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(isMapRendered) {
                LocationSearchField(
                    modifier = Modifier
                        .padding(bottom = Spacing.Medium),
                    query = query,
                    onQueryChange = onQueryChange,
                    onRequestLocationClick = onRequestLocationClick
                )
            }

            AnimatedVisibility(location != null && configurationType != ConfigurationType.EventCreation) {
                RadiusCard(
                    radiusInKm = radiusInKm,
                    onRadiusChange = onRadiusChange
                )
            }

            AnimatedVisibility(selectedLocation != null && isMapRendered) {
                selectedLocation?.let { LocationSelectionCard(locationSelection = it, onClick = onLocationCardClick) }
            }
        }
    }
}

@Composable
private fun MapContent(
    configurationType: ConfigurationType,
    location: LatLng?,
    radiusInKm: Int,
    areas: List<Area>,
    locationSelection: SelectedLocation?
) {
    if (configurationType != ConfigurationType.EventCreation) {
        location?.let { location ->
            RadiusCircle(
                radiusInKm = radiusInKm.toDouble(),
                location = location
            )
        }

        AreaMarks(areas = areas)
    } else {
        locationSelection?.let {
            AreaMarker(it.latitude, it.longitude, it.street)
        }
    }
}


@Composable
private fun AreaMarks(areas: List<Area>) {
    areas.forEach { area ->
        key(area.id) {
            AreaMarker(
                latitude = area.locationLatitude,
                longitude = area.locationLongitude,
                title = area.name
            )
        }
    }
}

@Composable
private fun AreaMarker(latitude: Double, longitude: Double, title: String) {
    Marker(
        state = rememberUpdatedMarkerState(
            position = LatLng(latitude, longitude)
        ),
        title = title,
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    )
}

@Composable
private fun RadiusCircle(
    radiusInKm: Double,
    location: LatLng
) {
    Circle(
        center = location,
        radius = radiusInKm * 1000,
        strokeColor = MaterialTheme.colorScheme.primary,
        strokeWidth = 2f,
        fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    )
}


@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        LocationPage(
            cameraPositionState = rememberCameraPositionState(),
            location = null,
            radiusInKm = 10,
            query = "",
            areas = emptyList(),
            configurationType = ConfigurationType.Default,
            selectedLocation = null,
            isMapRendered = true,
            isLoading = false,
            onMapReady = { },
            onQueryChange = { },
            onLocationChange = { },
            onRadiusChange = { },
            onRequestLocationClick = { },
            onLocationCardClick = {}
        )
    }
}