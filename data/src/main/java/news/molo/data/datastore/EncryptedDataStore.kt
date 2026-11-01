package news.molo.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.subtle.Base64
import de.convelop.forge.datastore.DataEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.io.File

private const val DATASTORE_NAME = "molo_secure_prefs"
private const val TINK_KEYSET_NAME = "secure_prefs_keyset"
private const val MASTER_KEY_URI = "android-keystore://news_molo_master_key"
private const val MASTER_KEY_PREF_NAME = "master_key_preference"
internal val corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }

val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME,
    corruptionHandler = corruptionHandler
)

class EncryptedDataStore(private val context: Context) : de.convelop.forge.datastore.DataStore {

    private val appContext = context.applicationContext

    private val aead: Aead by lazy {
        try {
            getOrRegisterAead()
        } catch (e: Exception) {
            e.printStackTrace()

            resetEncryptedStorage()

            try {
                getOrRegisterAead()
            } catch (fatalError: Exception) {
                throw RuntimeException("Fatal: Could not initialize EncryptedDataStore even after reset.", fatalError)
            }
        }
    }

    override suspend fun <T> get(dataEntry: DataEntry<T>): T? =
        observe(dataEntry).firstOrNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T> observe(dataEntry: DataEntry<T>): Flow<T> {
        val preferencesKey = stringPreferencesKey(dataEntry.key)

        return context.datastore.data
            .map { preferences -> preferences[preferencesKey] }
            .distinctUntilChanged()
            .mapLatest { encryptedValue ->

                if (encryptedValue == null) {
                    return@mapLatest dataEntry.defaultValue
                }

                withContext(Dispatchers.Default) {
                    val decryptedString = decrypt(encryptedValue, dataEntry.key)

                    if (decryptedString != null) {
                        try {
                            deserializeValue(decryptedString, dataEntry)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            dataEntry.defaultValue
                        }
                    } else {
                        dataEntry.defaultValue
                    }
                }
            }.flowOn(Dispatchers.IO)
    }

    override suspend fun <T> set(dataEntry: DataEntry<T>, value: T) {
        val encryptedResult = withContext(Dispatchers.Default) {
            val valueToString = serializeValue(value, dataEntry)
            encrypt(valueToString, dataEntry.key)
        }

        appContext.datastore.edit { preferences ->
            val preferencesKey = stringPreferencesKey(dataEntry.key)
            preferences[preferencesKey] = encryptedResult
        }
    }

    override suspend fun <T> delete(dataEntry: DataEntry<T>) {
        val preferencesKey = stringPreferencesKey(dataEntry.key)
        context.datastore.edit {
            it.remove(preferencesKey)
        }
    }

    private fun getOrRegisterAead(): Aead {
        TinkConfig.register()
        return AndroidKeysetManager.Builder()
            .withSharedPref(appContext, TINK_KEYSET_NAME, MASTER_KEY_PREF_NAME)
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }

    private fun encrypt(plainText: String, keyName: String): String {
        val bytes = aead.encrypt(
            plainText.toByteArray(Charsets.UTF_8),
            keyName.toByteArray(Charsets.UTF_8)
        )

        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun decrypt(encryptedDataBase64: String, keyName: String): String? {
        return try {
            val bytes = Base64.decode(encryptedDataBase64, Base64.NO_WRAP)
            val decryptedBytes = aead.decrypt(bytes, keyName.toByteArray(Charsets.UTF_8))
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    private fun <T> serializeValue(value: T, dataEntry: DataEntry<T>): String {
        return when (value) {
            is String -> value
            is Int, is Boolean, is Double -> value.toString()
            else -> {
                val serializer = dataEntry.serializer
                    ?: throw IllegalArgumentException("Serializer missing for custom type: ${dataEntry.key}")
                dataStoreJson.encodeToString(serializer, value)
            }
        }
    }

    private fun <T> deserializeValue(decryptedValue: String, dataEntry: DataEntry<T>): T {
        return when (val defaultValue = dataEntry.defaultValue) {
            is Int? -> decryptedValue.toIntOrNull() ?: defaultValue
            is Boolean? -> decryptedValue.toBooleanStrictOrNull() ?: defaultValue
            is Double? -> decryptedValue.toDoubleOrNull() ?: defaultValue
            is String -> decryptedValue
            else -> {
                val serializer = dataEntry.serializer
                    ?: throw IllegalArgumentException("Serializer must be provided for custom types.")
                dataStoreJson.decodeFromString(serializer, decryptedValue)
            }
        } as T
    }

    private fun resetEncryptedStorage() {
        try {
            appContext.getSharedPreferences(TINK_KEYSET_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit() // We need to await it here so dont use apply
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val dataStoreFile =
                File(appContext.filesDir, "datastore/$DATASTORE_NAME.preferences_pb")
            if (dataStoreFile.exists()) {
                dataStoreFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}