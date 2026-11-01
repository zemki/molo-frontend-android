package news.molo.feature.posting.event.creation.presentation

import android.net.Uri
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory
import java.time.OffsetDateTime

data class EventDraft(
    val userDisplayName: String = "",
    val userAvatarUrl: String? = null,
    val title: String = "",
    val content: String = "",
    val from: OffsetDateTime? = null,
    val until: OffsetDateTime? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val street: String = "",
    val zip: String = "",
    val town: String = "",
    val link: String = "",
    val ticketLink: String = "",
    val eventLocation: String = "",
    val imageUri: Uri? = null,
    val imageSource: String = "",
    val tagCategories: List<TagCategory> = emptyList()
) {

    val formattedLocation = buildString {
        if (street.isNotEmpty()) {
            append(street)
        }
        if (town.isNotEmpty()) {
            if (isNotEmpty()) {
                append(", ")
            }
            append(town)
        }
        if (zip.isNotEmpty()) {
            if (isNotEmpty()) {
                append(", ")
            }
            append(zip)
        }
    }
}

sealed class EventCreationState {

    open val draft: EventDraft? = null
    open val original: EventDraft? = null

    val hasChanges: Boolean
        get() = draft != original

    open val missingFields: List<String> = emptyList()

    sealed interface Intent {
        data object NavigateUp : Intent
        data class Post(val asDraft: Boolean = false) : Intent
    }

    data object Loading : EventCreationState()

    data class Creation(
        override val draft: EventDraft,
        override val original: EventDraft,
    ) : EventCreationState() {

        override val missingFields: List<String>
            get() = buildList {
                if (draft.title.isBlank()) add("Event- Titel")
                if (draft.content.isBlank()) add("Infos zur Veranstaltung: Wer, Was, Wann, Wo und warum?")
            }

        sealed interface Intent : EventCreationState.Intent {
            data class SetTitle(val value: String) : Intent
            data class SetContent(val value: String) : Intent
            data object ToConfiguration : Intent
            data object Back : Intent
        }
    }

    data class Configuration(
        override val draft: EventDraft,
        override val original: EventDraft,
    ) : EventCreationState() {

        override val missingFields: List<String>
            get() = buildList {
                if (draft.eventLocation.isBlank()) add("Spielstätte / Veranstaltungsort")
                if (draft.longitude == null || draft.latitude == null) add("Adresse")
                if (draft.from == null) add("Datum und Uhrzeit")
                if (draft.tagCategories.flatMap { it.tags }.none { it.isSelected }) add("Themen")
            }

        sealed interface Intent : EventCreationState.Intent {
            data class SetDateRange(val from: OffsetDateTime, val until: OffsetDateTime) : Intent
            data class SetLink(val value: String) : Intent
            data class SetTicketLink(val value: String) : Intent
            data class SetEventLocation(val value: String) : Intent
            data object ToPreview : Intent
            data object ToLocationSelection : Intent
            data class ToggleTagSelection(val tag: Tag) : Intent
            data class ToggleAllTagsSelection(val category: TagCategory) : Intent
            data object Back : Intent
        }
    }

    data class Preview(
        override val draft: EventDraft,
        override val original: EventDraft,
    ) : EventCreationState() {
        sealed interface Intent : EventCreationState.Intent {
            data object Back : Intent
            data class SetImage(val uri: Uri, val source: String) : Intent
        }
    }

    data object Updated : EventCreationState()
    data object DraftCreated : EventCreationState()
    data object Created : EventCreationState()
}