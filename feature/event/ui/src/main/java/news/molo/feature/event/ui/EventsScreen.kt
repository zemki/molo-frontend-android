package news.molo.feature.event.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.EventCategory
import news.molo.feature.event.presentation.EventFilter
import news.molo.feature.event.presentation.EventState
import news.molo.feature.event.presentation.EventsViewModel
import news.molo.feature.event.ui.page.EventsPage

@Composable
fun EventsScreen(
    viewModel: EventsViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    EventsScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent
    )
}

@Composable
private fun EventsScreenImpl(
    state: EventState,
    executeIntent: (EventState.Intent) -> Unit
) {
    EventsPage(
        query = state.query,
        filter = state.filter,
        selectedFilter = state.selectedFilter,
        categories = state.categories,
        pagingData = state.events,
        showManualRefresh = state.showManualRefresh,
        isLoading = state.isLoading,
        datePickerVisible = state.datePickerVisible,
        onEventClick = { executeIntent(EventState.Intent.ToEventDetails(it)) },
        onOrganizationClick = { executeIntent(EventState.Intent.ToOrganisation(it)) },
        onSelectDateRange = { from, to ->
            executeIntent(
                EventState.Intent.SelectFilter(
                    EventFilter.Calendar(
                        from,
                        to
                    )
                )
            )
        },
        onFilterSelectionChange = { executeIntent(EventState.Intent.SelectFilter(it)) },
        onRefreshEvents = { executeIntent(EventState.Intent.Refresh) },
        onQueryChange = { executeIntent(EventState.Intent.SetQuery(it)) },
        onDatePickerDismiss = { executeIntent(EventState.Intent.DismissDatePickerDialog) }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        EventsScreenImpl(
            state = EventState(categories = listOf(EventCategory.mock)),
            executeIntent = {}
        )
    }
}