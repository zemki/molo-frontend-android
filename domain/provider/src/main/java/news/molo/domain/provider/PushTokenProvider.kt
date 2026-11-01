package news.molo.domain.provider

interface PushTokenProvider {
    suspend fun getPushToken(): Result<String>
}