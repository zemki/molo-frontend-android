package news.molo.feature.event.ui.page

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import news.molo.core.designsystem.component.molecule.DateRangePickerModal
import news.molo.core.designsystem.component.organism.RefreshableList
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event
import news.molo.domain.model.EventCategory
import news.molo.feature.event.presentation.EventFilter
import news.molo.feature.event.presentation.EventState
import news.molo.feature.event.ui.component.EventCategoryRow
import news.molo.feature.event.ui.component.EventFilterRow
import java.time.OffsetDateTime

@Composable
internal fun EventsPage(
    query: String,
    filter: List<EventFilter>,
    selectedFilter: EventFilter?,
    categories: List<EventCategory>,
    pagingData: Map<Int, Flow<PagingData<Event>>>,
    showManualRefresh: Boolean,
    isLoading: Boolean,
    datePickerVisible: Boolean,
    onEventClick: (Int) -> Unit,
    onOrganizationClick: (Int) -> Unit,
    onSelectDateRange: (from: OffsetDateTime, to: OffsetDateTime) -> Unit,
    onFilterSelectionChange: (EventFilter) -> Unit,
    onRefreshEvents: () -> Unit,
    onQueryChange: (String) -> Unit,
    onDatePickerDismiss: () -> Unit
) {

    if (datePickerVisible) {
        DateRangePickerModal(
            onDateRangeSelected = onSelectDateRange,
            onDismiss = onDatePickerDismiss
        )
    }

    RefreshableList(
        isRefreshing = showManualRefresh,
        showLoading = isLoading,
        onRefresh = onRefreshEvents,
        header = {
            EventFilterRow(
                filter = filter,
                selectedFilter = selectedFilter,
                onSelectedChange = onFilterSelectionChange
            )
        }
    ) {
        items(categories, key = { it.id }) { category ->
            pagingData[category.id]?.let { pagingFlow ->
                EventCategoryRow(
                    category = category,
                    pagingFlow = pagingFlow,
                    onEventClick = onEventClick,
                    onOrganizationClick = onOrganizationClick
                )
            }
        }
    }
}


@ComponentPreview
@Composable
private fun Preview() {
    val state = EventState(categories = listOf(EventCategory.mock))

    ComponentPreviewTemplate {
        EventsPage(
            query = "abc",
            filter = EventFilter.all(),
            selectedFilter = null,
            categories = emptyList(),
            pagingData = emptyMap(),
            showManualRefresh = true,
            isLoading = false,
            datePickerVisible = false,
            onEventClick = { },
            onOrganizationClick = { },
            onSelectDateRange = { _, _ -> },
            onFilterSelectionChange = { },
            onRefreshEvents = { },
            onQueryChange = { },
            onDatePickerDismiss = { }
        )
    }
}