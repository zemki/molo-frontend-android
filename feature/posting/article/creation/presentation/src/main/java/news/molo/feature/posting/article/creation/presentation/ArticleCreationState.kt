package news.molo.feature.posting.article.creation.presentation

import android.net.Uri
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory

data class ArticleDraft(
    val headline: String = "",
    val content: String = "",
    val link: String = "",
    val imageUri: Uri? = null,
    val source: String? = null,
    val tagCategories: List<TagCategory> = emptyList(),
    val userDisplayName: String = "",
    val userAvatarUrl: String? = ""
)

sealed class ArticleCreationState {

    open val draft: ArticleDraft? = null
    open val original: ArticleDraft? = null

    val hasChanges: Boolean
        get() = draft != original

    open val missingFields: List<String> = emptyList()

    sealed interface Intent {
        data object NavigateUp : Intent

        data class Post(val asDraft: Boolean = false) : Intent

    }

    data object Loading : ArticleCreationState()

    data class Loaded(
        override val draft: ArticleDraft,
        override val original: ArticleDraft,
    ) : ArticleCreationState() {

        override val missingFields: List<String>
            get() = buildList {
                if (draft.headline.isBlank()) add("Überschrift / Titel")
                if (draft.content.isBlank()) add("Was gibt es Neues in deinem Ort?")
            }

        sealed interface Intent : ArticleCreationState.Intent {
            data class SetHeadline(val value: String) : Intent
            data class SetContent(val value: String) : Intent
            data object ToConfiguration : Intent
            data object Cancel : Intent
        }
    }

    data class Configuration(
        override val draft: ArticleDraft,
        override val original: ArticleDraft,
    ) : ArticleCreationState() {

        override val missingFields: List<String>
            get() = buildList {
                if (draft.tagCategories.flatMap { it.tags }.none { it.isSelected }) add("Themen")
            }

        sealed interface Intent : ArticleCreationState.Intent {
            data object NavigateBack : Intent
            data class SetLink(val value: String) : Intent
            data object ToPreview : Loaded.Intent
            data class ToggleTagSelection(val tag: Tag) : Preview.Intent
            data class ToggleAllTagsSelection(val category: TagCategory) : Preview.Intent
        }
    }

    data class Preview(
        override val draft: ArticleDraft,
        override val original: ArticleDraft
    ) : ArticleCreationState() {
        sealed interface Intent : ArticleCreationState.Intent {
            data class SetImage(val uri: Uri, val source: String) : Intent
            data object DeleteImage : Intent
            data object NavigateBack : Intent
        }
    }

    data object Updated : ArticleCreationState()
    data object DraftCreated : ArticleCreationState()
    data object Created : ArticleCreationState()
}