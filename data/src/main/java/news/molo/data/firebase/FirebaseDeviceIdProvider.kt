package news.molo.data.firebase

import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import news.molo.data.exception.DeviceIdFetchException
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.repository.SessionRepository

class FirebaseDeviceIdProvider(
    private val sessionRepository: SessionRepository
) : DeviceIdProvider {
    override suspend fun getDeviceId(): Result<String> {
        val deviceId = sessionRepository.getDeviceId().getOrNull()

        if (!deviceId.isNullOrBlank()) return Result.success(deviceId)

        val firebaseId = try {
            FirebaseInstallations.getInstance().id.await()
        } catch (e: Exception) {
            return Result.failure(
                DeviceIdFetchException(
                    "Failed to fetch Firebase Installation ID",
                    e
                )
            )
        }

        sessionRepository.setDeviceId(firebaseId)
        return Result.success(firebaseId)
    }
}