package news.molo.domain.provider

interface DeviceIdProvider {
    suspend fun getDeviceId(): Result<String>
}