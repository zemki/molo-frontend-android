package news.molo.feature.location.presentation

import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.ResultOutcome
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.mapSuccess
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import news.molo.core.event.UIEvent
import news.molo.core.navigation.NavigationResult
import news.molo.core.util.helper.getAddress
import news.molo.core.util.helper.getAddressesByName
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.Area
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.SelectedLocation
import news.molo.domain.usecase.area.GetAreasByDistanceUseCase
import news.molo.domain.usecase.area.GetSelectedAreaWithRadiusUseCase
import news.molo.domain.usecase.area.SelectAreaUseCase
import java.io.IOException

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class LocationViewModel(
    private val geocoder: Geocoder,
    private val configurationType: ConfigurationType,
    private val getSelectedAreaWithRadiusUseCase: GetSelectedAreaWithRadiusUseCase,
    private val selectAreaUseCase: SelectAreaUseCase,
    private val getAreasByDistanceUseCase: GetAreasByDistanceUseCase
) : BaseViewModel<LocationState, LocationState.Intent>(
    LocationState(), useEventBus = false
) {

    init {
        viewModelScope.launch {
            observeSearchQuery()
        }
    }

    override fun executeIntent(intent: LocationState.Intent) {
        when (intent) {
            is LocationState.Intent.MarkMapAsLoaded -> handleIntent<_, _>(
                intent = intent, handler = ::handleMarkMapLoadedIntent
            )

            is LocationState.Intent.SetLocation -> handleIntent<_, _>(
                intent = intent, handler = ::handleSetLocationIntent
            )

            is LocationState.Intent.SetRadius -> handleIntent<_, _>(
                intent = intent, handler = ::handleSetRadiusIntent
            )

            is LocationState.Intent.SetQuery -> {
                setState(currentState.copy(query = intent.query))
            }

            is LocationState.Intent.SubmitLocation -> handleIntent<_, _>(
                intent = intent, handler = ::handleSubmitLocationIntent
            )
        }
    }

    private fun handleMarkMapLoadedIntent(
        state: LocationState,
        intent: LocationState.Intent.MarkMapAsLoaded,
    ) {
        setState(currentState.copy(isMapRendered = true, isLoading = true))

        viewModelScope.launch {
            when (val selectedArea = getSelectedAreaWithRadiusUseCase()) {
                is ResultOutcome.Failure -> Log.d(
                    javaClass.name, "Failed to get selected area with radius"
                )

                is ResultOutcome.Success -> {
                    val area = selectedArea.data.area
                    val location = area?.let {
                        LatLng(it.locationLatitude, it.locationLongitude)
                    }

                    if (location == null || configurationType == ConfigurationType.EventCreation) {
                        setState(currentState.copy(isLoading = false))
                        return@launch
                    }

                    fetchAreasForLocation(location, radius = selectedArea.data.radius)
                }
            }
        }
    }

    private fun handleSetLocationIntent(
        state: LocationState,
        intent: LocationState.Intent.SetLocation,
    ) {

        when (configurationType) {
            ConfigurationType.EventCreation -> viewModelScope.launch {
                val address =
                    geocoder.getAddress(intent.location.latitude, intent.location.longitude)
                        ?.firstOrNull() ?: run {
                        sendUIEvent(UIEvent.SnackBar("Ort nicht gefunden."))
                        return@launch
                    }

                val newLatLng = LatLng(address.latitude, address.longitude)
                val newBounds = withContext(Dispatchers.Default) {
                    calculateBounds(
                        newLatLng, state.radiusInKm
                    )
                }

                setState(
                    state.copy(
                        cameraBounds = newBounds, locationSelection = SelectedLocation(
                            town = address.locality,
                            street = "${address.thoroughfare} ${address.subThoroughfare}",
                            zip = address.postalCode,
                            latitude = address.latitude,
                            longitude = address.longitude
                        )
                    )
                )
            }

            else -> fetchAreasForLocation(
                newLocation = intent.location
            )
        }
    }

    private fun handleSetRadiusIntent(
        state: LocationState,
        intent: LocationState.Intent.SetRadius,
    ) {
        viewModelScope.launch {
            val targetLocation =
                state.location ?: state.locationSelection?.let { LatLng(it.latitude, it.longitude) }
            val newBounds = withContext(Dispatchers.Default) {
                calculateBounds(targetLocation, intent.radiusInKm)
            }

            setState(
                currentState.copy(
                    radiusInKm = intent.radiusInKm,
                    cameraBounds = newBounds,
                    filteredAreas = filterAreasInRadius(
                        currentState.areasWithDistance, intent.radiusInKm
                    )
                )
            )
        }
    }

    private fun handleSubmitLocationIntent(
        state: LocationState,
        intent: LocationState.Intent.SubmitLocation,
    ) {
        when (configurationType) {
            ConfigurationType.EventCreation -> {
                state.locationSelection?.let {
                    dispatchNavigationEvent(
                        NavigationEvent.NavigateUpWithResult(
                            NavigationResult.LocationSelected, state.locationSelection
                        )
                    )
                }
            }

            else -> submitLocationSelection(state)
        }
    }

    private suspend fun observeSearchQuery() {
        collectStateFlow().map { it.query }.debounce(1000L)
            .filter { it.isNotBlank() && it.length > 2 }.distinctUntilChanged()
            .mapLatest { query -> searchLocation(query) }.collect { result ->
                handleSearchResult(result)
            }
    }

    private suspend fun handleSearchResult(result: LocationResult) {
        when (result) {
            is LocationResult.Error -> {
                sendUIEvent(UIEvent.SnackBar(result.message))
            }

            is LocationResult.Success -> {
                if (configurationType == ConfigurationType.EventCreation) {
                    val bounds = calculateBounds(
                        location = result.latLng, radiusKm = currentState.radiusInKm
                    )
                    setState(
                        currentState.copy(
                            cameraBounds = bounds,
                            locationSelection = result.address.toSelectedLocation()
                        )
                    )
                    return
                }

                fetchAreasForLocation(result.latLng)
            }
        }
    }

    private suspend fun searchLocation(query: String): LocationResult =
        withContext(Dispatchers.IO) {
            try {
                val addresses = geocoder.getAddressesByName(
                    locationName = query,
                    lowerLeftLatitude = MapConstants.GERMANY_BOUNDS.southwest.latitude,
                    lowerLeftLongitude = MapConstants.GERMANY_BOUNDS.southwest.longitude,
                    upperRightLatitude = MapConstants.GERMANY_BOUNDS.northeast.latitude,
                    upperRightLongitude = MapConstants.GERMANY_BOUNDS.northeast.longitude
                )

                val address = addresses?.firstOrNull()
                    ?: return@withContext LocationResult.Error("Ort muss innerhalb Deutschlands sein.")
                val latLng = LatLng(address.latitude, address.longitude)

                val isInGermany =
                    address.countryCode == "DE" && MapConstants.GERMANY_BOUNDS.contains(latLng)

                if (isInGermany) {
                    LocationResult.Success(address, latLng)
                } else {
                    LocationResult.Error("Ort muss innerhalb Deutschlands sein.")
                }
            } catch (e: IOException) {
                LocationResult.Error(e.message ?: "Ein Fehler ist aufgetreten.")
            }
        }

    private fun Address.isComplete(): Boolean {
        return !locality.isNullOrBlank() && !thoroughfare.isNullOrBlank() && !subThoroughfare.isNullOrBlank() && !postalCode.isNullOrBlank()
    }

    private fun Address.toSelectedLocation(): SelectedLocation {
        return SelectedLocation(
            town = locality,
            street = "$thoroughfare $subThoroughfare",
            zip = postalCode,
            latitude = latitude,
            longitude = longitude
        )
    }

    private fun submitLocationSelection(
        state: LocationState,
    ) {
        val closestAreas = state.filteredAreas.firstOrNull() ?: run {
            sendUIEvent(UIEvent.SnackBar("Bitte wählen ein Radius/Standort mit mindestens einem Gebiet"))
            return
        }

        viewModelScope.launch {
            selectAreaUseCase(
                SelectAreaUseCase.Params(
                    area = closestAreas, radius = state.radiusInKm
                )
            ).collectOutcome(
                onProgress = { setState(currentState.copy(isLoading = true)) },
                onSuccess = { dispatchNavigationEvent(NavigationEvent.NavigateUp) })
        }
    }

    private fun fetchAreasForLocation(
        newLocation: LatLng, radius: Int? = null
    ) {
        viewModelScope.launch {
            getAreasByDistanceUseCase(
                GetAreasByDistanceUseCase.Params(
                    latitude = newLocation.latitude,
                    longitude = newLocation.longitude,
                    radius = MapConstants.MAX_RADIUS
                )
            ).mapSuccess {
                it.withDistance(newLocation) to calculateBounds(
                    location = newLocation, radiusKm = radius ?: currentState.radiusInKm
                )
            }.collectOutcome(
                onProgress = { setState(currentState.copy(isLoading = true)) },
                onSuccess = { (areasWithDistance, newBounds) ->
                    setState(
                        currentState.copy(
                            location = newLocation,
                            isLoading = false,
                            cameraBounds = newBounds,
                            areasWithDistance = areasWithDistance,
                            radiusInKm = radius ?: currentState.radiusInKm,
                            filteredAreas = filterAreasInRadius(
                                areasWithDistance,
                                radius ?: currentState.radiusInKm,
                            )
                        )
                    )
                })
        }
    }

    private suspend fun List<Area>.withDistance(location: LatLng): List<Pair<Area, Double>> =
        withContext(Dispatchers.Default) {
            map { area ->
                val areaLocation = LatLng(area.locationLatitude, area.locationLongitude)
                val distance = SphericalUtil.computeDistanceBetween(location, areaLocation)
                Pair(area, distance)
            }
        }

    private fun filterAreasInRadius(
        areasWithDistance: List<Pair<Area, Double>>,
        radius: Int,
    ): List<Area> {
        val radiusInMeters = radius * 1000.0
        return areasWithDistance.filter { (_, distanceInMeters) -> distanceInMeters <= radiusInMeters }
            .map { (area, _) -> area }
    }

    private suspend fun calculateBounds(location: LatLng?, radiusKm: Int): LatLngBounds? {
        return withContext(Dispatchers.Default) {
            location ?: return@withContext null

            val radiusMeters = radiusKm * 1000.0
            val northeast = SphericalUtil.computeOffset(location, radiusMeters, 45.0)
            val southwest = SphericalUtil.computeOffset(location, radiusMeters, 225.0)

            LatLngBounds(southwest, northeast)
        }
    }
}

sealed interface LocationResult {
    data class Success(val address: Address, val latLng: LatLng) : LocationResult
    data class Error(val message: String) : LocationResult
}