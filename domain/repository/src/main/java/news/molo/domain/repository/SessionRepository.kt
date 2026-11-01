package news.molo.domain.repository

import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.AreaWithRadius
import news.molo.domain.model.ColorSchemaPreference

interface SessionRepository {
    suspend fun getDeviceId(): Result<String>
    suspend fun getAuthToken(): Result<String>
    suspend fun getSelectedAreaName(): Result<String>
    suspend fun getPushToken(): Result<String>
    suspend fun getSelectedLocationWithRadius(): Result<AreaWithRadius>
    suspend fun getSelfUserOrganisationId(): Result<Int>

    fun getColorSchemaAsFlow(): Flow<ColorSchemaPreference>
    fun getAuthTokenAsFlow(): Flow<String>
    fun getProfileReloadTriggerAsFlow(): Flow<Int>
    fun getIsOnboardingCompletedAsFlow(): Flow<Boolean>
    fun getSelectedLocationWithRadiusAsFlow(): Flow<AreaWithRadius>

    fun getPostingRefreshAsFlow(): Flow<Int>

    suspend fun setColorSchema(schemaPreference: ColorSchemaPreference)
    suspend fun setBearer(token: String)
    suspend fun setUserId(userId: Int)
    suspend fun triggerProfileReload()
    suspend fun triggerPostingReload()
    suspend fun setSelectedLocationWithRadius(location: AreaWithRadius)
    suspend fun setSelfUserOrganisationId(id: Int)
    suspend fun setDeviceId(deviceId: String)
    suspend fun setPushToken(token: String)
    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun deleteAuthToken()
}