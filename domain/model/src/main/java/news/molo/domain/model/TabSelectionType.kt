package news.molo.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class TabSelectionType {
    Article,
    Events
}