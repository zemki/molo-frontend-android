package news.molo.feature.event.detail.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.navigation.Screen
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.event.BookmarkEventUseCase
import news.molo.domain.usecase.event.FlagEventUseCase
import news.molo.domain.usecase.event.GetEventDetailsUseCase
import news.molo.domain.usecase.event.RemoveEventBookmarkUseCase

class DetailedEventViewModel(
    private val eventId: Int,
    getEventDetailsUseCase: GetEventDetailsUseCase,
    private val removeBookmarkUseCase: RemoveEventBookmarkUseCase,
    private val bookmarkEventUseCase: BookmarkEventUseCase,
    private val flagEventUseCase: FlagEventUseCase
) : BaseViewModel<DetailedEventState, DetailedEventState.Intent>(
    DetailedEventState.Loading,
    useEventBus = false
) {

    private val eventDetails = getEventDetailsUseCase(eventId)
        .onEachOutcome(
            onProgress = { setState(DetailedEventState.Loading) },
            onFailure = { dispatchNavigationEvent(NavigationEvent.NavigateUp) },
            onSuccess = { setState(DetailedEventState.Loaded(it)) }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: DetailedEventState.Intent) {
        when (intent) {
            is DetailedEventState.Loaded.Intent.ToggleBookmark -> handleIntent<_, _>(
                intent,
                ::handleToggleBookmark
            )

            is DetailedEventState.Loaded.Intent.ReportEvent -> handleIntent<_, _>(
                intent,
                ::handleReport
            )

            DetailedEventState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            is DetailedEventState.Loaded.Intent.ToOrganisation -> handleIntent<_, _>(
                intent = intent,
                ::handleToOrganisation
            )
        }
    }

    private fun handleToOrganisation(
        state: DetailedEventState.Loaded,
        intent: DetailedEventState.Loaded.Intent.ToOrganisation,
    ) {
        dispatchNavigationEvent(
            NavigationEvent.NavigateTo(
                Screen.OrganizationDetails(
                    organizationId = state.event.organization,
                    defaultTagSelectionType = TabSelectionType.Events
                )
            )
        )
    }

    private fun handleToggleBookmark(
        state: DetailedEventState.Loaded,
        intent: DetailedEventState.Loaded.Intent.ToggleBookmark,
    ) {
        viewModelScope.launch {
            when (state.event.isBookmarked) {
                true -> removeBookmarkUseCase(eventId)
                false -> bookmarkEventUseCase(eventId)
            }.collectOutcome(
                onProgress = { setState(DetailedEventState.Loading) },
                onFailure = {
                    sendUIEvent(UIEvent.SnackBar("Event konnte nicht gespeichert werden"))
                    setState(state)
                },
                onSuccess = { setState(state.copy(event = state.event.copy(isBookmarked = !state.event.isBookmarked))) }
            )
        }
    }

    private fun handleReport(
        state: DetailedEventState.Loaded,
        intent: DetailedEventState.Loaded.Intent.ReportEvent,
    ) {
        viewModelScope.launch {
            flagEventUseCase(eventId).collectOutcome(
                onFailure = { showSnackbar(it.message) },
                onSuccess = { showSnackbar("Event wurde gemeldet.") }
            )
        }
    }
}