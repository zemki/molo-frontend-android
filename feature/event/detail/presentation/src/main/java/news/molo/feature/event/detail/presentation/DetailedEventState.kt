package news.molo.feature.event.detail.presentation

import news.molo.domain.model.Event

sealed interface DetailedEventState {

    sealed interface Intent {
        data object NavigateUp : Intent
    }

    data object Loading : DetailedEventState

    data class Loaded(val event: Event) : DetailedEventState {
        sealed interface Intent : DetailedEventState.Intent {
            data object ToggleBookmark : Intent
            data object ReportEvent : Intent
            data object ToOrganisation : Intent
        }
    }

}