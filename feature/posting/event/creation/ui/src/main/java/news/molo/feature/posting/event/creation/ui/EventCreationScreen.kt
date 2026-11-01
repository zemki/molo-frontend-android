package news.molo.feature.posting.event.creation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import news.molo.core.designsystem.component.atom.CreationTopBar
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.molecule.MissingFieldsAlert
import news.molo.core.designsystem.component.molecule.UnsavedChangesAlert
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.posting.event.creation.presentation.EventCreationState
import news.molo.feature.posting.event.creation.presentation.EventCreationState.Creation.Intent.SetContent
import news.molo.feature.posting.event.creation.presentation.EventCreationState.Creation.Intent.SetTitle
import news.molo.feature.posting.event.creation.presentation.EventCreationState.Preview.Intent.SetImage
import news.molo.feature.posting.event.creation.presentation.EventCreationViewModel
import news.molo.feature.posting.event.creation.presentation.EventDraft
import java.time.OffsetDateTime

@Composable
fun EventCreationScreen(
    viewModel: EventCreationViewModel
) {

    val state by viewModel.collectStateFlow().collectAsState()

    EventCreationScreenImpl(
        state = state,
        events = viewModel.events,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCreationScreenImpl(
    state: EventCreationState,
    events: Flow<UIEvent>,
    executeIntent: (EventCreationState.Intent) -> Unit
) {

    var showUnsavedChangesAlert by remember { mutableStateOf(false) }
    var showMissingFieldsAlert by remember { mutableStateOf(false) }

    BackHandler(state.hasChanges) {
        showUnsavedChangesAlert = true
    }

    BackHandler(state is EventCreationState.Preview) {
        executeIntent(EventCreationState.Preview.Intent.Back)
    }

    BackHandler(state is EventCreationState.Configuration) {
        executeIntent(EventCreationState.Configuration.Intent.Back)
    }

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    UIEventHandler(
        uiEvent = events,
        snackBarHostState = snackBarHostState
    )

    if (showUnsavedChangesAlert) {
        UnsavedChangesAlert(
            onDismiss = {
                showUnsavedChangesAlert = false
            },
            onConfirmSave = {
                showUnsavedChangesAlert = false
                executeIntent(EventCreationState.Intent.Post(asDraft = true))
            },
            onDiscard = {
                showUnsavedChangesAlert = false
                executeIntent(EventCreationState.Intent.NavigateUp)
            }
        )
    }

    if (showMissingFieldsAlert) {
        MissingFieldsAlert(
            missingFields = state.missingFields,
            onDismiss = { showMissingFieldsAlert = false }
        )
    }

    ScreenScaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            EventCreationTopBar(
                state = state,
                executeIntent = executeIntent,
                onShowUnsaveChangesAlert = { showUnsavedChangesAlert = true },
                onShowMissingFieldsAlert = { showMissingFieldsAlert = true }
            )
        }
    ) {
        when (state) {
            is EventCreationState.Creation -> EventCreationPage(
                headline = state.draft.title,
                content = state.draft.content,
                onHeadlineChange = { executeIntent(SetTitle(it)) },
                onContentChange = { executeIntent(SetContent(it)) },
                profilePictureUrl = state.draft.userAvatarUrl
            )

            EventCreationState.Loading -> CoveringProgressIndicator()
            is EventCreationState.Preview -> PreviewPage(
                headline = state.draft.title,
                displayName = state.draft.userDisplayName,
                content = state.draft.content,
                imageUri = state.draft.imageUri,
                onSelectImage = { image, source ->
                    executeIntent(
                        SetImage(
                            uri = image,
                            source = source
                        )
                    )
                },
                eventLocation = state.draft.eventLocation,
                startDate = state.draft.from ?: OffsetDateTime.now(),
                city = state.draft.town,
                imageSource = state.draft.imageSource,
                onClickPost = { executeIntent(EventCreationState.Intent.Post()) },
                onClickSaveDraft = { executeIntent(EventCreationState.Intent.Post(asDraft = true)) }
            )

            is EventCreationState.Configuration -> EventConfigurationPage(
                location = state.draft.formattedLocation,
                startDate = state.draft.from,
                endDate = state.draft.until,
                onChangeLocationClick = {
                    executeIntent(EventCreationState.Configuration.Intent.ToLocationSelection)
                },
                onDateRangeChange = { from, until ->
                    executeIntent(EventCreationState.Configuration.Intent.SetDateRange(from, until))
                },
                link = state.draft.link,
                ticketLink = state.draft.ticketLink,
                eventLocation = state.draft.eventLocation,
                onLinkChange = {
                    executeIntent(EventCreationState.Configuration.Intent.SetLink(it))
                },
                onTicketLinkChange = {
                    executeIntent(EventCreationState.Configuration.Intent.SetTicketLink(it))
                },
                onEventLocationChange = {
                    executeIntent(EventCreationState.Configuration.Intent.SetEventLocation(it))
                },
                tagCategories = state.draft.tagCategories,
                onSelectTag = {
                    executeIntent(EventCreationState.Configuration.Intent.ToggleTagSelection(it))
                },
                onSelectCategory = {
                    executeIntent(EventCreationState.Configuration.Intent.ToggleAllTagsSelection(it))
                }
            )

            EventCreationState.Created -> EventUpdatedPage(
                headline = stringResource(R.string.event_posting_created_title),
                text = stringResource(R.string.event_posting_created_description),
                onClickNext = {
                    executeIntent(EventCreationState.Intent.NavigateUp)
                }
            )

            EventCreationState.DraftCreated -> EventUpdatedPage(
                headline = stringResource(R.string.event_posting_created_title),
                text = stringResource(R.string.event_posting_created_description),
                onClickNext = {
                    executeIntent(EventCreationState.Intent.NavigateUp)
                }
            )

            EventCreationState.Updated -> EventUpdatedPage(
                headline = stringResource(R.string.event_posting_updated_title),
                text = stringResource(R.string.event_posting_updated_title),
                onClickNext = { executeIntent(EventCreationState.Intent.NavigateUp) }
            )
        }
    }
}

@Composable
private fun EventCreationTopBar(
    state: EventCreationState,
    executeIntent: (EventCreationState.Intent) -> Unit,
    onShowUnsaveChangesAlert: () -> Unit,
    onShowMissingFieldsAlert: () -> Unit
) {
    when (state) {
        is EventCreationState.Creation -> CreationTopBar(
            onBackClick = {
                when (state.hasChanges) {
                    true -> onShowUnsaveChangesAlert()
                    false -> executeIntent(EventCreationState.Creation.Intent.Back)
                }
            },
            onNextClick = {
                when (state.missingFields.isEmpty()) {
                    true -> executeIntent(EventCreationState.Creation.Intent.ToConfiguration)
                    false -> onShowMissingFieldsAlert()
                }
            }
        )

        is EventCreationState.Preview -> DefaultTopAppBar(
            onNavigateBackClick = { executeIntent(EventCreationState.Preview.Intent.Back) }
        )

        is EventCreationState.Configuration -> CreationTopBar(
            onBackClick = {
                executeIntent(EventCreationState.Configuration.Intent.Back)
            },
            onNextClick = {
                when (state.missingFields.isEmpty()) {
                    true -> executeIntent(EventCreationState.Configuration.Intent.ToPreview)
                    false -> onShowMissingFieldsAlert()
                }
            }
        )

        else -> {}
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        EventCreationScreenImpl(
            state = EventCreationState.Creation(draft = EventDraft(), original = EventDraft()),
            events = emptyFlow(),
            executeIntent = { }
        )
    }
}