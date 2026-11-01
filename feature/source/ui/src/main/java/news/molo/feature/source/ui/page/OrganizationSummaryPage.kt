package news.molo.feature.source.ui.page

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import news.molo.core.designsystem.component.molecule.ExpandableOrganizationSummaryHeader
import news.molo.core.designsystem.component.molecule.OrganizationCard
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ExpandableListView
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Category
import news.molo.domain.model.Organization
import news.molo.domain.model.OrganizationSummary

@Composable
internal fun OrganizationSummaryPage(
    summaries: List<OrganizationSummary>,
    expandedSummaryNames: Set<String>,
    onSummaryClick: (OrganizationSummary) -> Unit,
    onOrganisationClick: (Organization) -> Unit,
    onToggleFollowingClick: (Organization) -> Unit
) {
    PageTemplate {
        ExpandableListView(
            items = summaries,
            itemContent = { summary, isExpanded ->
                ExpandableOrganizationSummaryHeader(
                    title = summary.name,
                    iconRes = when (summary.category) {
                        Category.NEWS -> R.drawable.icn_news
                        Category.COMMUNITY -> R.drawable.icn_community
                        Category.OFFICIAL -> R.drawable.icn_offical
                    },
                    isExpanded = isExpanded,
                    onClick = { onSummaryClick(summary) },
                )
            },
            itemKey = { it.name },
            expandedItemKeys = expandedSummaryNames,
            expandedContent = { summary ->
                items(
                    items = summary.organizations,
                    key = { it.id }
                ) { organization ->
                    OrganizationCard(
                        name = organization.name,
                        imageURL = organization.imageURL,
                        isFollowing = organization.isSelected,
                        onFollowingChanged = { onToggleFollowingClick(organization) },
                        onClick = { onOrganisationClick(organization) }
                    )
                }
            }
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        OrganizationSummaryPage(
            summaries = listOf(OrganizationSummary.mock),
            expandedSummaryNames = emptySet(),
            onSummaryClick = { },
            onToggleFollowingClick = { },
            onOrganisationClick = {}
        )
    }
}

