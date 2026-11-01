package news.molo.data.datastore

import kotlinx.serialization.json.Json

internal val dataStoreJson by lazy {
    Json { ignoreUnknownKeys = true; coerceInputValues = true; encodeDefaults = true }
}