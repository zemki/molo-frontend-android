package news.molo.feature.bookmark.ui.page

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import news.molo.core.designsystem.component.molecule.EventCard
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ListTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event

@Composable
internal fun BookmarkedEventsPage(
    eventPagingDataFlow: Flow<PagingData<Event>>,
    onEventClick: (Int) -> Unit,
    onOrganizationClick: (Int) -> Unit,
) {

    val pagingData = eventPagingDataFlow.collectAsLazyPagingItems()

    PageTemplate {
        ListTemplate {
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
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        BookmarkedEventsPage(
            eventPagingDataFlow = flowOf(PagingData.from(listOf(Event.mock, Event.mock))),
            onEventClick = {},
            onOrganizationClick = { },
        )
    }
}