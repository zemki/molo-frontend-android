package news.molo.feature.posting.event.creation.presentation

import news.molo.domain.model.SelectedLocation

sealed interface EventCreationNavResult {
    data class Location(val value: SelectedLocation) : EventCreationNavResult
}