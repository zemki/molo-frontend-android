package news.molo.feature.area.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent.*
import de.convelop.forge.outcome.onEachOutcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import news.molo.core.navigation.NavigationResult
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.usecase.area.GetAreasUseCase

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class AreaSelectionViewModel(
    private val getAreasUseCase: GetAreasUseCase
) : BaseViewModel<AreaSelectionState, AreaSelectionState.Intent>(
    initialState = AreaSelectionState.Loading
) {

    private val reloadTrigger = MutableSharedFlow<String>(replay = 1)

    private val areas = reloadTrigger
        .onStart { emit("") }
        .debounce(500)
        .flatMapLatest { query -> getAreasUseCase(query) }
        .onEachOutcome(
            onSuccess = { data ->
                reduceStateOrCreate<AreaSelectionState.Loaded>(
                    reducer = { copy(areas = data) },
                    create = { AreaSelectionState.Loaded(areas = data) }
                )
            }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: AreaSelectionState.Intent) {
        when (intent) {
            is AreaSelectionState.Loaded.Intent.Query -> handleIntent<_, _>(
                intent,
                ::handleQueryIntent
            )

            is AreaSelectionState.Loaded.Intent.NavigateUpWithResult -> dispatchNavigationEvent(
                NavigateUpWithResult(
                    NavigationResult.SelectedArea,
                    intent.area
                )
            )

            AreaSelectionState.Intent.NavigateUp -> dispatchNavigationEvent(NavigateUp)
        }
    }

    private fun handleQueryIntent(
        state: AreaSelectionState.Loaded,
        intent: AreaSelectionState.Loaded.Intent.Query,
    ) {
        setState(state.copy(query = intent.value))
        reloadAreas(query = intent.value)
    }

    private fun reloadAreas(query: String) = reloadTrigger.tryEmit(query)

}