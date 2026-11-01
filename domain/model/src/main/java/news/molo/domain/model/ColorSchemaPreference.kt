package news.molo.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ColorSchemaPreference {
    Dark,
    Light,
    System
}