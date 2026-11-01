package news.molo.feature.area.presentation

import news.molo.domain.model.Area

sealed interface AreaSelectionState {

    sealed interface Intent {
        data object NavigateUp : Intent
    }

    data object Loading : AreaSelectionState

    data class Loaded(
        val areas: List<Area>,
        val query: String = ""
    ) : AreaSelectionState {
        sealed interface Intent : AreaSelectionState.Intent {
            data class Query(val value: String) : Intent

            data class NavigateUpWithResult(val area: Area) : Intent
        }
    }
}