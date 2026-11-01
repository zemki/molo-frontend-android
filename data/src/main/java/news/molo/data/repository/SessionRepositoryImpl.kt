package news.molo.data.repository

import de.convelop.forge.datastore.DataStore
import kotlinx.coroutines.flow.Flow
import news.molo.data.datastore.MoloDataEntry
import news.molo.domain.model.AreaWithRadius
import news.molo.domain.model.ColorSchemaPreference
import news.molo.domain.repository.SessionRepository
import kotlin.random.Random

class SessionRepositoryImpl(
    private val dataStore: DataStore
) : SessionRepository {

    override suspend fun getDeviceId(): Result<String> {
        return dataStore.get(MoloDataEntry.DeviceID).toResult()
    }

    override suspend fun getAuthToken(): Result<String> {
        return dataStore.get(MoloDataEntry.AuthToken).toResult()
    }

    override suspend fun getSelectedAreaName(): Result<String> {
        return dataStore.get(MoloDataEntry.SelectedAreaName).toResult()
    }

    override suspend fun getPushToken(): Result<String> {
        return dataStore.get(MoloDataEntry.PushToken).toResult()
    }

    override suspend fun getSelectedLocationWithRadius(): Result<AreaWithRadius> {
        return dataStore.get(MoloDataEntry.SelectedAreaWithRadius).toResult()
    }

    override suspend fun getSelfUserOrganisationId(): Result<Int> {
        return dataStore.get(MoloDataEntry.SelfUserOrganisationId).toResult()
    }

    override fun getColorSchemaAsFlow(): Flow<ColorSchemaPreference> {
        return dataStore.observe(MoloDataEntry.ColorSchema)
    }

    override fun getAuthTokenAsFlow(): Flow<String> {
        return dataStore.observe(MoloDataEntry.AuthToken)
    }

    override fun getProfileReloadTriggerAsFlow(): Flow<Int> {
        return dataStore.observe(MoloDataEntry.ProfileRefreshTrigger)
    }

    override fun getIsOnboardingCompletedAsFlow(): Flow<Boolean> {
        return dataStore.observe(MoloDataEntry.OnboardingCompleted)
    }

    override fun getSelectedLocationWithRadiusAsFlow(): Flow<AreaWithRadius> {
        return dataStore.observe(MoloDataEntry.SelectedAreaWithRadius)
    }

    override fun getPostingRefreshAsFlow(): Flow<Int> {
        return dataStore.observe(MoloDataEntry.PostingRefreshTrigger)
    }

    override suspend fun setColorSchema(schemaPreference: ColorSchemaPreference) {
        dataStore.set(MoloDataEntry.ColorSchema, schemaPreference)
    }

    override suspend fun setBearer(token: String) {
        dataStore.set(MoloDataEntry.AuthToken, token)
    }

    override suspend fun setUserId(userId: Int) {
        dataStore.set(MoloDataEntry.UserID, userId)
    }

    override suspend fun triggerProfileReload() {
        dataStore.set(MoloDataEntry.ProfileRefreshTrigger, Random.nextInt())
    }

    override suspend fun triggerPostingReload() {
        dataStore.set(MoloDataEntry.PostingRefreshTrigger, Random.nextInt())
    }

    override suspend fun setSelectedLocationWithRadius(location: AreaWithRadius) {
        dataStore.set(MoloDataEntry.SelectedAreaWithRadius, location)
    }

    override suspend fun setSelfUserOrganisationId(id: Int) {
        dataStore.set(MoloDataEntry.SelfUserOrganisationId, id)
    }

    override suspend fun setDeviceId(deviceId: String) {
        dataStore.set(MoloDataEntry.DeviceID, deviceId)
    }

    override suspend fun setPushToken(token: String) {
        dataStore.set(MoloDataEntry.PushToken, token)
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.set(MoloDataEntry.OnboardingCompleted, completed)
    }

    override suspend fun deleteAuthToken() {
        dataStore.delete(MoloDataEntry.AuthToken)
    }

    fun <T> T?.toResult(
        exception: Throwable = NullPointerException("Value was null")
    ): Result<T> {
        return if (this != null) {
            Result.success(this)
        } else {
            Result.failure(exception)
        }
    }
}