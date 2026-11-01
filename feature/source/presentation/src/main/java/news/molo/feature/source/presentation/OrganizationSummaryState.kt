package news.molo.feature.source.presentation

import news.molo.domain.model.Organization
import news.molo.domain.model.OrganizationSummary

sealed interface OrganizationSummaryState {
    sealed interface Intent {
        data object NavigateUp : Intent
    }

    data object Loading : OrganizationSummaryState

    data class Loaded(
        val summaries: List<OrganizationSummary>,
        val expandedSummaryNames: Set<String> = emptySet()
    ) : OrganizationSummaryState {

        sealed interface Intent : OrganizationSummaryState.Intent {
            data class ToggleOrganizationSelection(val organization: Organization) : Intent
            data class ToggleSummaryExpansion(val summary: OrganizationSummary) : Intent
            data class NavigateToOrganizationDetails(val organisationId: Int) : Intent
        }
    }
}