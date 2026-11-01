package news.molo.core.util.helper

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Suppress("DEPRECATION")
suspend fun Geocoder.getAddress(
    latitude: Double,
    longitude: Double,
    maxResults: Int = 1
): List<Address>? = suspendCancellableCoroutine { continuation ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocation(latitude, longitude, maxResults) { addresses ->
            continuation.resume(addresses)
        }
    } else {
        try {
            val addresses = getFromLocation(latitude, longitude, maxResults)
            continuation.resume(addresses)
        } catch (e: Exception) {
            continuation.resume(null)
        }
    }
}

@Suppress("DEPRECATION")
suspend fun Geocoder.getAddressesByName(
    locationName: String,
    maxResults: Int = 1,
    lowerLeftLatitude: Double,
    lowerLeftLongitude: Double,
    upperRightLatitude: Double,
    upperRightLongitude: Double
): List<Address>? = suspendCancellableCoroutine { continuation ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocationName(
            locationName,
            maxResults,
            lowerLeftLatitude,
            lowerLeftLongitude,
            upperRightLatitude,
            upperRightLongitude
        ) { addresses ->
            continuation.resume(addresses)
        }
    } else {
        try {
            val addresses = getFromLocationName(
                locationName,
                maxResults,
                lowerLeftLatitude,
                lowerLeftLongitude,
                upperRightLatitude,
                upperRightLongitude
            )
            continuation.resume(addresses)
        } catch (e: Exception) {
            continuation.resume(null)
        }
    }
}