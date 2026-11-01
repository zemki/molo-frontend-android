package news.molo.feature.organization.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import news.molo.core.designsystem.component.molecule.EventCard
import news.molo.core.designsystem.component.molecule.PostDeletionAlert
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event

@Composable
internal fun OrganizationEventPage(
    pagingDataFlow: Flow<PagingData<Event>>,
    listState: LazyListState = rememberLazyListState(),
    areEventsDeletable: Boolean,
    onEventClick: (Int) -> Unit,
    onDeleteClick: ((Int) -> Unit)? = null,
) {

    val pagingData = pagingDataFlow.collectAsLazyPagingItems()
    val isEmpty = pagingData.loadState.refresh is LoadState.NotLoading && pagingData.itemCount == 0

    var eventIdToDelete by rememberSaveable { mutableStateOf<Int?>(null) }

    if (eventIdToDelete != null) {
        PostDeletionAlert(
            onConfirm = {
                onDeleteClick?.invoke(eventIdToDelete ?: 0)
                eventIdToDelete = null
            },
            onDismiss = { eventIdToDelete = null }
        )
    }


    when (isEmpty) {
        true -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.event_no_results_placeholder_text))
        }

        false -> LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(count = pagingData.itemCount) { index ->
                pagingData[index]?.let { event ->
                    EventCard(
                        title = event.title,
                        content = event.content,
                        date = event.eventDate,
                        location = event.eventLocation,
                        city = event.town,
                        organizationName = event.organizationName,
                        imageURL = event.imageURL,
                        isDraft = event.isDraft,
                        onClick = { onEventClick(event.id) },
                        onClickPublisher = {},
                        onClickDelete = if (areEventsDeletable) {
                            {
                                eventIdToDelete = event.id
                            }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        OrganizationEventPage(
            pagingDataFlow = flow { PagingData.from(listOf(Event.mock)) },
            onEventClick = {},
            areEventsDeletable = true
        )
    }
}