package news.molo.feature.search.result.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.molecule.EventCard
import news.molo.core.designsystem.component.organism.RefreshableList
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.util.paging.rememberManualRefreshState
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event

@Composable
internal fun EventsSearchResultPage(
    eventPagingDataFlow: Flow<PagingData<Event>>,
    onEventClick: (Int) -> Unit,
    onOrganizationClick: (Int) -> Unit,
    onRefresh: () -> Unit
) {

    val pagingData = eventPagingDataFlow.collectAsLazyPagingItems()
    val refreshState = rememberManualRefreshState(pagingData.loadState.refresh)
    var hasInitialLoadStarted by remember { mutableStateOf(false) }

    LaunchedEffect(pagingData.loadState.refresh) {
        if (pagingData.loadState.refresh is LoadState.Loading) {
            hasInitialLoadStarted = true
        }
    }
    val isListEmpty = hasInitialLoadStarted &&
            pagingData.loadState.refresh is LoadState.NotLoading &&
            pagingData.itemCount == 0

    PageTemplate {
        RefreshableList(
            isRefreshing = refreshState.isRefreshing,
            showLoading = refreshState.showLoading,
            isEmpty = isListEmpty,
            onRefresh = {
                refreshState.start()
                onRefresh()
            },
            emptyContent = {
                NoSearchResultsScreen()
            },
            content = {
                items(count = pagingData.itemCount) { index ->
                    val event = pagingData[index] ?: return@items

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
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        EventsSearchResultPage(
            eventPagingDataFlow = flowOf(PagingData.from(listOf(Event.mock, Event.mock))),
            onEventClick = {},
            onOrganizationClick = { },
            onRefresh = {}
        )
    }
}