package news.molo.feature.source.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.navigation.Screen
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.OrganizationSummary
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.organization.DisableOrganizationUseCase
import news.molo.domain.usecase.organization.EnableOrganizationUseCase
import news.molo.domain.usecase.organization.GetOrganizationSummaryUseCase

class OrganizationSummaryViewModel(
    getOrganizationSummaryUseCase: GetOrganizationSummaryUseCase,
    private val disableOrganizationUseCase: DisableOrganizationUseCase,
    private val enableOrganizationUseCase: EnableOrganizationUseCase
) : BaseViewModel<OrganizationSummaryState, OrganizationSummaryState.Intent>(
    OrganizationSummaryState.Loading,
    useEventBus = false
) {

    private val organizationSummary = getOrganizationSummaryUseCase()
        .onEachOutcome(
            onProgress = { setState(OrganizationSummaryState.Loading) },
            onFailure = { showSnackbar(it.message) },
            onSuccess = { setState(OrganizationSummaryState.Loaded(it)) }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: OrganizationSummaryState.Intent) {
        when (intent) {
            is OrganizationSummaryState.Loaded.Intent.ToggleOrganizationSelection -> handleIntent<_, _>(
                intent,
                ::handleToggleOrganizationSelection
            )

            is OrganizationSummaryState.Loaded.Intent.ToggleSummaryExpansion -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleSummaryExpansion
            )

            OrganizationSummaryState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            is OrganizationSummaryState.Loaded.Intent.NavigateToOrganizationDetails -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleNavigateToOrganizationDetails
            )
        }
    }

    private fun handleToggleOrganizationSelection(
        state: OrganizationSummaryState.Loaded,
        intent: OrganizationSummaryState.Loaded.Intent.ToggleOrganizationSelection,
    ) {

        val updatedSummaries = state.summaries.toggleSelection(intent.organization.id)

        setState(state.copy(summaries = updatedSummaries))

        viewModelScope.launch {
            when (intent.organization.isSelected) {
                true -> disableOrganizationUseCase(intent.organization.id)
                false -> enableOrganizationUseCase(intent.organization.id)
            }.collectOutcome(
                onFailure = { showSnackbar(it.message) },
                onSuccess = { Log.d(javaClass.name, "Submitted organization selection") }
            )
        }
    }

    private fun handleToggleSummaryExpansion(
        state: OrganizationSummaryState.Loaded,
        intent: OrganizationSummaryState.Loaded.Intent.ToggleSummaryExpansion,
    ) {

        val expandedSummaries = state.expandedSummaryNames

        val newExpandedSummaryNames = when (expandedSummaries.contains(intent.summary.name)) {
            true -> expandedSummaries - intent.summary.name
            false -> expandedSummaries + intent.summary.name
        }

        setState(state.copy(expandedSummaryNames = newExpandedSummaryNames))
    }

    private fun handleNavigateToOrganizationDetails(
        state: OrganizationSummaryState.Loaded,
        intent: OrganizationSummaryState.Loaded.Intent.NavigateToOrganizationDetails,
    ) {
        dispatchNavigationEvent(
            event = NavigationEvent.NavigateTo(
                destination = Screen.OrganizationDetails(
                    organizationId = intent.organisationId,
                    defaultTagSelectionType = TabSelectionType.Article
                )
            )
        )
    }

    private fun List<OrganizationSummary>.toggleSelection(organizationId: Int) = map { summary ->
        val updatedOrganizations = summary.organizations.map { organization ->
            if (organization.id == organizationId) {
                organization.copy(isSelected = !organization.isSelected)
            } else {
                organization
            }
        }
        summary.copy(organizations = updatedOrganizations)
    }
}