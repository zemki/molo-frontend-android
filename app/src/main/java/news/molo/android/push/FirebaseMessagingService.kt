package news.molo.android.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import news.molo.domain.usecase.user.PostNewPushTokenUseCase
import org.koin.android.ext.android.inject

class FirebaseMessagingService : FirebaseMessagingService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val postNewPushTokenUseCase by inject<PostNewPushTokenUseCase>()

    override fun onNewToken(token: String) {
        scope.launch {
            postNewPushTokenUseCase().collectOutcome(
                onProgress = { Log.d("FCM", "Posting token...") },
                onFailure = { Log.d("FCM", "Failed to post token") },
                onSuccess = { Log.d("FCM", "Posted token") }
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "Message received")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}