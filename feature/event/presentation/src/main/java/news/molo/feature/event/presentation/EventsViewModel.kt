package news.molo.feature.event.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.Outcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import news.molo.core.event.UIEvent
import news.molo.core.navigation.Screen
import news.molo.core.util.paging.createPager
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.EventCategory
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.error.MoloError
import news.molo.domain.usecase.event.GetPaginatedEventsUseCase
import news.molo.domain.usecase.event.ObserveEventCategoriesWithEventsUseCase

class EventsViewModel(
    observeEventCategoriesWithEventsUseCase: ObserveEventCategoriesWithEventsUseCase,
    private val getPaginatedEventsUseCase: GetPaginatedEventsUseCase
) : BaseViewModel<EventState, EventState.Intent>(EventState(isLoading = true)) {

    private val reloadTrigger = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val filterFlow = MutableSharedFlow<EventFilter?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val getEventCategories = combine(
        reloadTrigger.onStart { emit("") },
        filterFlow.onStart { emit(null) }
    ) { query, filter ->
        query to filter
    }
        .debounce(500)
        .flatMapLatest { (query, filter) ->
            observeEventCategoriesWithEventsUseCase().map { outcome ->
                Triple(query, filter, outcome)
            }
        }
        .onEach { (query, filter, outcome) ->
            insertPagersForResult(
                outcome = outcome,
                filter = filter,
                query = currentState.query
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )


    override fun executeIntent(intent: EventState.Intent) {
        when (intent) {
            is EventState.Intent.SelectFilter -> handleIntent<_, _>(
                intent = intent,
                ::handleSelectFilter
            )

            is EventState.Intent.DismissDatePickerDialog -> handleIntent<_, _>(
                intent = intent,
                ::handleDismissDatePickerDialog
            )

            is EventState.Intent.SetQuery -> {
                reduceState<EventState> {
                    reloadTrigger.tryEmit(query)
                    copy(query = intent.value)
                }
            }

            is EventState.Intent.Refresh -> {
                setState(state = currentState.copy(showManualRefresh = true))
                reloadTrigger.tryEmit(currentState.query)
            }

            is EventState.Intent.ToEventDetails -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(
                    Screen.EventDetails(
                        intent.eventId
                    )
                )
            )

            is EventState.Intent.ToOrganisation -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(
                    Screen.OrganizationDetails(
                        intent.organisationId,
                        TabSelectionType.Events
                    )
                )
            )
        }
    }

    private fun handleDismissDatePickerDialog(
        state: EventState,
        intent: EventState.Intent.DismissDatePickerDialog,
    ) {
        setState(state.copy(datePickerVisible = false))
    }

    private fun handleSelectFilter(
        state: EventState,
        intent: EventState.Intent.SelectFilter,
    ) {

        val isIncomingCalendar = intent.filter is EventFilter.Calendar
        val isSelectedCalendar = state.selectedFilter is EventFilter.Calendar

        if (isIncomingCalendar && isSelectedCalendar) {
            val updatedFilters = state.filter.map {
                if (it is EventFilter.Calendar) EventFilter.Calendar() else it
            }
            setState(state.copy(filter = updatedFilters, selectedFilter = null))
            filterFlow.tryEmit(null)
            return
        }

        if (isIncomingCalendar && !state.datePickerVisible) {
            setState(state.copy(datePickerVisible = true))
            return
        }

        if (intent.filter == state.selectedFilter) {
            val updatedFilters = state.filter.map {
                if (it is EventFilter.Calendar) EventFilter.Calendar() else it
            }

            setState(state.copy(filter = updatedFilters, selectedFilter = null))
            filterFlow.tryEmit(null)
            return
        }

        val updatedFilters = state.filter.map {
            if (it is EventFilter.Calendar) {
                if (isIncomingCalendar) intent.filter else EventFilter.Calendar()
            } else it
        }

        setState(state.copy(filter = updatedFilters, selectedFilter = intent.filter))
        filterFlow.tryEmit(intent.filter)
    }

    private fun insertPagersForResult(
        outcome: Outcome<List<EventCategory>, MoloError>,
        filter: EventFilter?,
        query: String,
    ) {
        when (outcome) {
            is Outcome.Progress -> {
                setState(currentState.copy(isLoading = true))
            }

            is Outcome.Failure -> {
                setState(currentState.copy(isLoading = false, categories = emptyList()))
                sendUIEvent(UIEvent.SnackBar("Error loading categories"))
            }

            is Outcome.Success -> {
                val newPagersMap = outcome.data.associate { category ->
                    val (from, to) = filter?.getDateRange() ?: (null to null)

                    val pager = createPager(
                        getItemsUseCase = getPaginatedEventsUseCase,
                        paramsFactory = { pageSize, offset ->
                            GetPaginatedEventsUseCase.Params(
                                pageSize = pageSize,
                                offset = offset,
                                categoryId = category.id,
                                query = query,
                                from = from,
                                to = to
                            )
                        }
                    )
                    category.id to pager.flow.cachedIn(viewModelScope)
                }

                setState(
                    currentState.copy(
                        isLoading = false,
                        showManualRefresh = false,
                        categories = outcome.data,
                        events = newPagersMap
                    )
                )
            }
        }
    }
}