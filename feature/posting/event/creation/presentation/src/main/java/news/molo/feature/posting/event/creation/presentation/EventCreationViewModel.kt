package news.molo.feature.posting.event.creation.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.navigation.Screen.*
import news.molo.core.viewmodel.BaseViewModelWithNavResult
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.EventCreationData
import news.molo.domain.model.withSelectedTags
import news.molo.domain.usecase.event.CreateEventUseCase
import news.molo.domain.usecase.event.GenerateEventTagsUseCase
import news.molo.domain.usecase.event.GetEventCreationDataUseCase
import news.molo.domain.usecase.event.GetEventUpdateDataUseCase
import news.molo.domain.usecase.event.UpdateEventUseCase
import java.time.OffsetDateTime

class EventCreationViewModel(
    private val eventId: Int? = null,
    private val generateEventTagsUseCase: GenerateEventTagsUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val getEventUpdateDataUseCase: GetEventUpdateDataUseCase,
    private val getEventCreationDataUseCase: GetEventCreationDataUseCase
) : BaseViewModelWithNavResult<EventCreationState, EventCreationState.Intent, EventCreationNavResult>(
    initialState = EventCreationState.Loading,
    useEventBus = false
) {

    init {
        if (eventId == null) getCreationData() else getUpdateData(eventId)
    }

    private fun getCreationData() {
        viewModelScope.launch {
            getEventCreationDataUseCase()
                .collectOutcome(
                    onProgress = { setState(EventCreationState.Loading) },
                    onFailure = { showSnackbar(it.message) },
                    onSuccess = { (user, tags) ->
                        val draft = EventDraft(
                            tagCategories = tags,
                            userDisplayName = user.displayName,
                            userAvatarUrl = user.imageUrl,
                            imageSource = user.displayName
                        )
                        setState(
                            EventCreationState.Creation(
                                draft = draft,
                                original = draft
                            )
                        )
                    }
                )
        }
    }

    private fun getUpdateData(eventId: Int) {
        viewModelScope.launch {
            getEventUpdateDataUseCase(eventId)
                .collectOutcome(
                    onProgress = { setState(EventCreationState.Loading) },
                    onFailure = { showSnackbar(it.message) },
                    onSuccess = { (event, tags) ->
                        val draft = EventDraft(
                            userDisplayName = event.organizationName,
                            imageSource = event.imageSourceName.takeUnless { it.isEmpty() }
                                .orEmpty(),
                            tagCategories = tags,
                            title = event.title,
                            content = event.content,
                            imageUri = event.imageURL.takeUnless { it.isEmpty() }?.toUri(),
                            town = event.town,
                            street = event.street,
                            zip = event.zipCode,
                            from = event.eventStartDate,
                            until = event.eventEndDate,
                            link = event.link,
                            eventLocation = event.eventLocation,
                            ticketLink = event.ticketURL,
                            latitude = event.latitude,
                            longitude = event.longitude
                        )
                        setState(
                            EventCreationState.Creation(
                                draft = draft,
                                original = draft
                            )
                        )
                    }
                )
        }
    }

    override fun executeIntent(intent: EventCreationState.Intent) {
        when (intent) {
            is EventCreationState.Creation.Intent.SetContent -> updateDraft { it.copy(content = intent.value) }

            is EventCreationState.Preview.Intent.SetImage -> updateDraft {
                it.copy(
                    imageUri = intent.uri,
                    imageSource = intent.source
                )
            }

            EventCreationState.Creation.Intent.Back -> dispatchNavigationEvent(
                NavigationEvent.NavigateUp
            )

            is EventCreationState.Creation.Intent.ToConfiguration -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToConfiguration
            )

            is EventCreationState.Preview.Intent.Back -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleBackFromPreview
            )

            is EventCreationState.Creation.Intent.SetTitle -> updateDraft { it.copy(title = intent.value) }

            is EventCreationState.Configuration.Intent.ToggleAllTagsSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleAllTagsSelection
            )

            is EventCreationState.Configuration.Intent.ToggleTagSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleTagSelection
            )

            is EventCreationState.Intent.Post -> handleIntent<_, _>(
                intent = intent,
                handler = ::handlePost
            )

            EventCreationState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            is EventCreationState.Configuration.Intent.ToPreview -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToPreview
            )

            is EventCreationState.Configuration.Intent.SetDateRange -> updateDraft {
                it.copy(
                    from = intent.from,
                    until = intent.until
                )
            }

            is EventCreationState.Configuration.Intent.ToLocationSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToLocationSelection
            )

            is EventCreationState.Configuration.Intent.SetEventLocation -> updateDraft {
                it.copy(eventLocation = intent.value)
            }

            is EventCreationState.Configuration.Intent.SetLink -> updateDraft { it.copy(link = intent.value) }

            is EventCreationState.Configuration.Intent.SetTicketLink -> updateDraft {
                it.copy(ticketLink = intent.value)
            }

            is EventCreationState.Configuration.Intent.Back -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleBackFromConfiguration
            )
        }
    }

    private fun handleBackFromPreview(
        state: EventCreationState.Preview,
        intent: EventCreationState.Preview.Intent.Back
    ) {
        setState(EventCreationState.Configuration(draft = state.draft, original = state.original))

    }

    private fun handleBackFromConfiguration(
        state: EventCreationState.Configuration,
        intent: EventCreationState.Configuration.Intent.Back
    ) {
        setState(EventCreationState.Creation(draft = state.draft, original = state.original))

    }

    private fun handleToLocationSelection(
        state: EventCreationState.Configuration,
        intent: EventCreationState.Configuration.Intent.ToLocationSelection
    ) {

        val draft = state.draft

        dispatchNavigationEvent(
            NavigationEvent.NavigateTo(
                LocationSelection(
                    configurationType = ConfigurationType.EventCreation,
                    initialLatitude = draft.latitude,
                    initialLongitude = draft.longitude
                )
            )
        )
    }

    private fun handleToPreview(
        state: EventCreationState.Configuration,
        intent: EventCreationState.Configuration.Intent.ToPreview
    ) {
        setState(EventCreationState.Preview(draft = state.draft, original = state.original))
    }

    private fun handleToggleTagSelection(
        state: EventCreationState.Configuration,
        intent: EventCreationState.Configuration.Intent.ToggleTagSelection,
    ) {
        val updatedTags = state.draft.tagCategories.withSelectedTags { tag ->
            if (tag.id == intent.tag.id) !tag.isSelected else tag.isSelected
        }

        setState(state.copy(draft = state.draft.copy(tagCategories = updatedTags)))
    }

    private fun handleToggleAllTagsSelection(
        state: EventCreationState.Configuration,
        intent: EventCreationState.Configuration.Intent.ToggleAllTagsSelection,
    ) {
        val tags = state.draft.tagCategories.map { tagCategory ->
            if (tagCategory.id == intent.category.id) {
                val shouldSelectAll = tagCategory.tags.any { !it.isSelected }

                tagCategory.copy(
                    tags = tagCategory.tags.map { tag ->
                        tag.copy(isSelected = shouldSelectAll)
                    }
                )
            } else {
                tagCategory
            }
        }

        setState(state = state.copy(draft = state.draft.copy(tagCategories = tags)))
    }

    private fun handleToConfiguration(
        state: EventCreationState.Creation,
        intent: EventCreationState.Creation.Intent.ToConfiguration
    ) {

        val current = state.draft

        viewModelScope.launch {
            when (current.title.isNotBlank() && current.content.isNotBlank()) {
                true -> generateEventTagsUseCase(
                    GenerateEventTagsUseCase.Params(
                        title = current.title, content = current.content
                    )
                ).collectOutcome(onSuccess = { selectedTags ->
                    val selectedTagIds = selectedTags + current.tagCategories.flatMap { it.tags }
                        .filter { it.isSelected }.map { it.id }

                    val preselectedTags = current.tagCategories.withSelectedTags {
                        selectedTagIds.contains(
                            it.id
                        )
                    }

                    setState(
                        EventCreationState.Configuration(
                            draft = current.copy(
                                tagCategories = preselectedTags
                            ), original = state.original
                        )
                    )
                }, onProgress = { setState(EventCreationState.Loading) })

                false -> setState(
                    EventCreationState.Configuration(
                        draft = current, original = state.original
                    )
                )
            }
        }
    }

    private fun handlePost(
        state: EventCreationState,
        intent: EventCreationState.Intent.Post
    ) {

        val draft = state.draft ?: run {
            sendUIEvent(UIEvent.SnackBar("Das sollte nicht passieren. Bitte melde diesen Fehler."))
            return
        }

        viewModelScope.launch {
            if (eventId == null) {
                createEventUseCase(
                    CreateEventUseCase.Params(
                        event = EventCreationData(
                            id = -1,
                            title = draft.title,
                            content = draft.content,
                            tags = draft.tagCategories.flatMap { it.tags.filter { it.isSelected } },
                            from = draft.from ?: OffsetDateTime.now(),
                            until = draft.until ?: OffsetDateTime.now(),
                            street = draft.street,
                            zip = draft.zip,
                            city = draft.town,
                            eventLocation = draft.eventLocation,
                            link = draft.link,
                            ticketLink = draft.ticketLink,
                            latitude = draft.latitude ?: 0.0,
                            longitude = draft.longitude ?: 0.0,
                            saveAsDraft = intent.asDraft
                        ),
                        pictureUrl = draft.imageUri.takeUnless { it?.scheme == "https" }
                            ?.toString(),
                        pictureSource = draft.imageSource
                    )
                )
            } else {
                updateEventUseCase(
                    UpdateEventUseCase.Params(
                        event = EventCreationData(
                            id = eventId,
                            title = draft.title,
                            content = draft.content,
                            tags = draft.tagCategories.flatMap { it.tags.filter { it.isSelected } },
                            from = draft.from ?: OffsetDateTime.now(),
                            until = draft.until ?: OffsetDateTime.now(),
                            street = draft.street,
                            zip = draft.zip,
                            city = draft.town,
                            eventLocation = draft.eventLocation,
                            link = draft.link,
                            ticketLink = draft.ticketLink,
                            latitude = draft.latitude ?: 0.0,
                            longitude = draft.longitude ?: 0.0,
                            saveAsDraft = intent.asDraft
                        ),
                        pictureUrl = draft.imageUri.takeUnless { it?.scheme == "https" }
                            ?.toString(),
                        pictureSource = draft.imageSource
                    )
                )
            }.collectOutcome(
                onProgress = { setState(EventCreationState.Loading) },
                onFailure = {
                    setState(state)
                    showSnackbar(it.message)
                },
                onSuccess = {
                    setState(
                        if (eventId == null) {
                            EventCreationState.Created
                        } else {
                            EventCreationState.Updated
                        }
                    )
                }
            )
        }
    }

    override fun onNavResultReceived(event: EventCreationNavResult) {
        when (event) {
            is EventCreationNavResult.Location -> updateDraft {
                it.copy(
                    street = event.value.street,
                    town = event.value.town,
                    zip = event.value.zip,
                    latitude = event.value.latitude,
                    longitude = event.value.longitude
                )
            }
        }
    }

    private fun updateDraft(reducer: (EventDraft) -> EventDraft) {
        val currentDraft = when (val s = currentState) {
            is EventCreationState.Creation -> s.draft
            is EventCreationState.Configuration -> s.draft
            is EventCreationState.Preview -> s.draft
            else -> return
        }

        val newDraft = reducer(currentDraft)

        val newState = when (val s = currentState) {
            is EventCreationState.Creation -> s.copy(draft = newDraft)
            is EventCreationState.Configuration -> s.copy(draft = newDraft)
            is EventCreationState.Preview -> s.copy(draft = newDraft)
            else -> s
        }
        setState(newState)
    }
}
