package news.molo.core.navigation

import de.convelop.forge.navigation.NavResultKey
import kotlinx.serialization.KSerializer
import news.molo.domain.model.Area
import news.molo.domain.model.SelectedLocation

sealed class NavigationResult<T>(
    key: String,
    serializer: KSerializer<T>? = null
) : NavResultKey<T>(key = key, serializer = serializer) {

    data object SelectedArea : NavigationResult<Area>(
        key = "selected_area",
        serializer = Area.serializer()
    )

    data object LocationSelected : NavigationResult<SelectedLocation>(
        key = "location_selected",
        serializer = SelectedLocation.serializer()
    )
}