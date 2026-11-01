package news.molo.feature.event.presentation

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import news.molo.domain.model.Event
import news.molo.domain.model.EventCategory

data class EventState(
    val query: String = "",
    val categories: List<EventCategory> = emptyList(),
    val filter: List<EventFilter> = EventFilter.all(),
    val events: Map<Int, Flow<PagingData<Event>>> = emptyMap(),
    val selectedFilter: EventFilter? = null,
    val datePickerVisible: Boolean = false,
    val isLoading: Boolean = false,
    val showManualRefresh: Boolean = false
) {

    sealed interface Intent {
        data object Refresh : Intent
        data class SelectFilter(val filter: EventFilter) : Intent
        data class SetQuery(val value: String) : Intent
        data object DismissDatePickerDialog : Intent

        data class ToEventDetails(val eventId: Int) : Intent
        data class ToOrganisation(val organisationId: Int) : Intent
    }
}
