package news.molo.feature.location.ui


import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import news.molo.core.designsystem.component.molecule.ContinueFab
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.ConfigurationType
import news.molo.feature.location.presentation.LocationState
import news.molo.feature.location.presentation.LocationViewModel
import news.molo.core.resource.R

private val GERMANY_CENTER_FALLBACK = LatLng(51.16549, 10.45153)

@Composable
fun LocationScreen(
    configurationType: ConfigurationType,
    viewModel: LocationViewModel,
    initialLatLng: Pair<Double, Double>?
) {
    val state by viewModel.collectStateFlow().collectAsState()

    BackHandler(configurationType == ConfigurationType.Onboarding) { }

    LaunchedEffect(initialLatLng) {
        initialLatLng?.let {
            viewModel.executeIntent(
                LocationState.Intent.SetLocation(
                    LatLng(
                        it.first,
                        it.second
                    )
                )
            )
        }
    }

    LocationScreenImpl(
        state = state,
        configurationType = configurationType,
        events = viewModel.events,
        executeIntent = viewModel::executeIntent,
        shouldRequestLocationOnLoad = initialLatLng == null && configurationType == ConfigurationType.Onboarding
    )
}

@Composable
private fun LocationScreenImpl(
    state: LocationState,
    configurationType: ConfigurationType,
    events: Flow<UIEvent>,
    executeIntent: (LocationState.Intent) -> Unit,
    shouldRequestLocationOnLoad: Boolean
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val showContinueButton = remember(state.location, state.isLoading, configurationType) {
        (state.location != null || configurationType == ConfigurationType.EventCreation) && !state.isLoading
    }

    UIEventHandler(
        uiEvent = events,
        snackBarHostState = snackBarHostState
    )

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            handleUserLocation(locationClient, executeIntent)
        } else {
            Toast.makeText(context, "Standort nicht freigegeben.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(state.isMapRendered, shouldRequestLocationOnLoad) {
        if (state.isMapRendered && shouldRequestLocationOnLoad && state.location == null) {
            requestLocationPermissions(permissionLauncher)
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(GERMANY_CENTER_FALLBACK, 5f)
    }

    LaunchedEffect(state.cameraBounds) {
        state.cameraBounds?.let { bounds ->
            cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 200))
        }
    }

    ScreenScaffold(
        modifier = Modifier.imePadding(),
        contentWindowInsets = WindowInsets(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showContinueButton
            ) {
                ContinueFab(
                    text = stringResource(R.string.location_next_button),
                    onClick = { executeIntent(LocationState.Intent.SubmitLocation) }
                )
            }
        }
    ) {

        LocationPage(
            cameraPositionState = cameraPositionState,
            location = state.location,
            radiusInKm = state.radiusInKm,
            query = state.query,
            areas = state.filteredAreas,
            configurationType = configurationType,
            selectedLocation = state.locationSelection,
            isMapRendered = state.isMapRendered,
            isLoading = state.isLoading,
            onMapReady = { executeIntent(LocationState.Intent.MarkMapAsLoaded) },
            onQueryChange = { executeIntent(LocationState.Intent.SetQuery(it)) },
            onLocationChange = { executeIntent(LocationState.Intent.SetLocation(it)) },
            onRadiusChange = { executeIntent(LocationState.Intent.SetRadius(it)) },
            onRequestLocationClick = { requestLocationPermissions(permissionLauncher) },
            onLocationCardClick = { executeIntent(LocationState.Intent.SubmitLocation) }
        )
    }
}

@SuppressLint("MissingPermission")
private fun handleUserLocation(
    locationClient: FusedLocationProviderClient,
    executeIntent: (LocationState.Intent) -> Unit
) {
    locationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            executeIntent(
                LocationState.Intent.SetLocation(
                    LatLng(it.latitude, it.longitude)
                )
            )
        }
    }
}

private fun requestLocationPermissions(permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>) {
    permissionLauncher.launch(
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = LocationState()
    ComponentPreviewTemplate {
        LocationScreenImpl(
            state = state,
            configurationType = ConfigurationType.Default,
            events = flow { },
            executeIntent = {},
            shouldRequestLocationOnLoad = false
        )
    }
}