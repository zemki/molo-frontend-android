package news.molo.android

import android.app.Application
import android.util.Log
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.Firebase
import com.google.firebase.initialize
import news.molo.android.di.appModule
import news.molo.core.util.map.MapStyleManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MoloApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST) {
            Log.d(javaClass.name, "Google Play services initialized.")
        }

        MapStyleManager.init(applicationContext)

        startKoin(appDeclaration = {
            androidContext(this@MoloApp)
            androidLogger()
            modules(appModule)
        })
    }
}