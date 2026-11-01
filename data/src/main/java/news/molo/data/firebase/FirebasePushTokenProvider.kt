package news.molo.data.firebase

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import news.molo.data.exception.DeviceIdFetchException
import news.molo.domain.provider.PushTokenProvider
import news.molo.domain.repository.SessionRepository

class FirebasePushTokenProvider(
    private val sessionRepository: SessionRepository
) : PushTokenProvider {
    override suspend fun getPushToken(): Result<String> {

        val pushToken = try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            return Result.failure(
                DeviceIdFetchException(
                    "Failed to fetch FCM Token",
                    e
                )
            )
        }

        sessionRepository.setPushToken(pushToken)
        return Result.success(pushToken)
    }
}