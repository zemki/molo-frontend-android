package news.molo.feature.organization.presentation

import news.molo.core.resource.R
import news.molo.domain.model.Organization
import news.molo.domain.model.TabSelectionType

sealed interface DetailedOrganizationState {

    sealed interface Intent {
        data object NavigateUp : Intent
    }

    data object Loading : DetailedOrganizationState

    data class Loaded(
        val organization: Organization,
        val tabs: List<TabSelectionType> = TabSelectionType.entries,
        val selectedTab: TabSelectionType = TabSelectionType.Article
    ) : DetailedOrganizationState {


        fun titleForType(type: TabSelectionType): Int {
            return when (type) {
                TabSelectionType.Article if (organization.isOwnedBySelfUser) -> R.string.organization_headline_my_articles
                TabSelectionType.Events if (organization.isOwnedBySelfUser) -> R.string.organization_headline_my_events
                TabSelectionType.Article -> R.string.organization_headline_article
                TabSelectionType.Events -> R.string.organization_headline_event
            }
        }


        sealed interface Intent : DetailedOrganizationState.Intent {
            data class SetSelectedTab(val tab: TabSelectionType) : Intent

            data class ToEvent(val eventId: Int) : Intent
            data class ToArticle(val articleId: Int) : Intent
            data object ToProfile : Intent

            data class DeleteArticle(val articleId: Int) : Intent
            data class DeleteEvent(val eventId: Int) : Intent
        }
    }
}