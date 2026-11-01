package news.molo.feature.event.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.atom.ProgressIndicator
import news.molo.core.designsystem.component.molecule.EventCard
import news.molo.core.designsystem.component.organism.HorizontalSnappingList
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.calculatePercentageOfWindowHeight
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event
import news.molo.domain.model.EventCategory

@Composable
internal fun EventCategoryRow(
    category: EventCategory,
    pagingFlow: Flow<PagingData<Event>>,
    onEventClick: (eventId: Int) -> Unit,
    onOrganizationClick: (orgId: Int) -> Unit,
) {

    val pagingData = pagingFlow.collectAsLazyPagingItems()

    val height = calculatePercentageOfWindowHeight(0.4f)
    val pagerState = rememberPagerState { pagingData.itemCount }

    HorizontalSnappingList(
        pagerState = pagerState,
        headline = {
            Text(
                modifier = Modifier.padding(horizontal = Spacing.Medium),
                text = category.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        placeholder = { LoadingPlaceholder(height) },
        isLoading = pagingData.loadState.refresh is LoadState.Loading,
        loadingContent = { LoadingContent(height) },
        content = { index ->
            pagingData[index]?.let { event ->
                EventCard(
                    title = event.title,
                    content = event.content,
                    date = event.eventDate,
                    location = event.eventLocation,
                    city = event.town,
                    imageURL = event.imageURL,
                    organizationName = event.organizationName,
                    onClick = { onEventClick(event.id) },
                    onClickPublisher = { onOrganizationClick(event.organization) }
                )
            }
        }
    )
}
@Composable
private fun LoadingPlaceholder(height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.event_no_events_placeholder))
    }
}

@Composable
private fun LoadingContent(height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        ProgressIndicator()
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        EventCategoryRow(
            category = EventCategory.mock,
            pagingFlow = flowOf(PagingData.from(listOf(Event.mock))),
            onEventClick = {},
            onOrganizationClick = {}
        )
    }
}