package news.molo.feature.event.detail.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.util.helper.addEventToCalendar
import news.molo.core.util.helper.shareEvent
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event
import news.molo.feature.event.detail.presentation.DetailedEventState
import news.molo.feature.event.detail.presentation.DetailedEventViewModel
import news.molo.feature.event.detail.ui.component.DetailedEventTopAppBar
import news.molo.feature.event.detail.ui.page.DetailedEventPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedEventScreen(
    viewModel: DetailedEventViewModel
) {

    val state by viewModel.collectStateFlow().collectAsStateWithLifecycle()

    DetailedEventScreenImpl(
        state = state,
        uiEvents = viewModel.events,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailedEventScreenImpl(
    state: DetailedEventState,
    uiEvents: SharedFlow<UIEvent>,
    executeIntent: (DetailedEventState.Intent) -> Unit
) {

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val isBookmarked = (state as? DetailedEventState.Loaded)?.event?.isBookmarked == true

    val onAddToCalendar: (Event) -> Unit = { event ->
        context.addEventToCalendar(
            title = event.title,
            description = event.content,
            startDate = event.eventStartDate,
            endDate = event.eventEndDate,
            location = event.eventLocation
        )
    }

    val onShareEvent: (Event) -> Unit = { event ->
        context.shareEvent(
            eventDate = event.eventDate,
            title = event.title,
            link = event.link
        )
    }

    UIEventHandler(
        uiEvent = uiEvents,
        snackBarHostState = snackBarHostState,
        onEventHandled = { executeIntent(DetailedEventState.Intent.NavigateUp) }
    )

    when (state) {
        is DetailedEventState.Loaded -> ScreenScaffold(
            topBar = {
                DetailedEventTopAppBar(
                    isBookmarked = isBookmarked,
                    onNavigateBackClicked = { executeIntent(DetailedEventState.Intent.NavigateUp) },
                    onShareClicked = { onShareEvent(state.event) },
                    onBookmarkedClicked = { executeIntent(DetailedEventState.Loaded.Intent.ToggleBookmark) },
                    onReportClicked = { executeIntent(DetailedEventState.Loaded.Intent.ReportEvent) },
                    onAddToCalendarClicked = { onAddToCalendar(state.event) }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        ) {
            DetailedEventPage(
                event = state.event,
                onOrganizerClick = { executeIntent(DetailedEventState.Loaded.Intent.ToOrganisation) },
            )
        }

        DetailedEventState.Loading -> CoveringProgressIndicator()
    }
}

private class Provider : PreviewParameterProvider<DetailedEventState> {
    override val values = sequenceOf(
        DetailedEventState.Loaded(Event.mock),
        DetailedEventState.Loading
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: DetailedEventState
) {

    ComponentPreviewTemplate {
        DetailedEventScreenImpl(
            state = state,
            uiEvents = MutableSharedFlow(),
            executeIntent = {}
        )
    }
}

