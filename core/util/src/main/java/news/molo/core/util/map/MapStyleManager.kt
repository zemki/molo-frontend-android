package news.molo.core.util.map

import android.content.Context
import com.google.android.gms.maps.model.MapStyleOptions
import news.molo.core.resource.R

object MapStyleManager {
    private var darkStyle: MapStyleOptions? = null

    fun init(context: Context) {
        try {
            darkStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getStyle(isDarkTheme: Boolean): MapStyleOptions? {
        return if (isDarkTheme) darkStyle else null
    }
}