package news.molo.feature.tags.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.TabSelectionType
import news.molo.domain.model.TagCategory
import news.molo.domain.usecase.article.GetArticleTagsUseCase
import news.molo.domain.usecase.article.UpdateArticleTagSelectionUseCase
import news.molo.domain.usecase.event.GetEventTagsUseCase
import news.molo.domain.usecase.event.UpdateEventTagSelectionUseCase

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class TagSelectionViewModel(
    defaultSelectionType: TabSelectionType,
    tagConfigurationType: ConfigurationType = ConfigurationType.Default,
    getArticleTagsUseCase: GetArticleTagsUseCase,
    getEventTagsUseCase: GetEventTagsUseCase,
    private val updateArticleTagSelectionUseCase: UpdateArticleTagSelectionUseCase,
    private val updateEventTagSelectionUseCase: UpdateEventTagSelectionUseCase
) : BaseViewModel<TagSelectionState, TagSelectionState.Intent>(
    TagSelectionState(
        selectedTab = defaultSelectionType,
        tagConfigurationType = tagConfigurationType
    )
) {

    private val articleTagStream = MutableSharedFlow<List<TagCategory>>(extraBufferCapacity = 5)
    private val eventTagStream = MutableSharedFlow<List<TagCategory>>(extraBufferCapacity = 5)

    private val articleTags = getArticleTagsUseCase()
        .onEachOutcome(
            onFailure = { showSnackbar(it.message) },
            onSuccess = { setState(currentState.copy(articleTagCategories = it)) }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val eventTags = getEventTagsUseCase()
        .onEachOutcome(
            onFailure = { showSnackbar(it.message) },
            onSuccess = { setState(currentState.copy(eventTagCategories = it)) }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        viewModelScope.launch {
            launch { collectArticleTagChangesAndSubmit() }
            launch { collectEventTagChangesAndSubmit() }
        }
    }

    override fun executeIntent(intent: TagSelectionState.Intent) {
        when (intent) {
            is TagSelectionState.Intent.SetSelectedTab -> {
                setState(currentState.copy(selectedTab = intent.tab))
            }

            TagSelectionState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            is TagSelectionState.Intent.ToggleAllArticleTagsSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleAllArticleTagsSelection
            )

            is TagSelectionState.Intent.ToggleAllEventTagsSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleAllEventTagsSelection
            )

            is TagSelectionState.Intent.ToggleArticleTagSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleArticleTagSelection
            )

            is TagSelectionState.Intent.ToggleEventTagSelection -> handleIntent<_, _>(
                intent = intent,
                handler = ::handleToggleEventTagSelection
            )
        }
    }

    private fun handleToggleArticleTagSelection(
        state: TagSelectionState,
        intent: TagSelectionState.Intent.ToggleArticleTagSelection,
    ) {
        val updatedTags = state.articleTagCategories.toggleTagSelection(intent.tag.id)

        setState(state = state.copy(articleTagCategories = updatedTags))
        articleTagStream.tryEmit(updatedTags)
    }

    private fun handleToggleAllArticleTagsSelection(
        state: TagSelectionState,
        intent: TagSelectionState.Intent.ToggleAllArticleTagsSelection,
    ) {
        val updatedTags = state.articleTagCategories.toggleCategorySelection(intent.category.id)

        setState(state = state.copy(articleTagCategories = updatedTags))
        articleTagStream.tryEmit(updatedTags)
    }

    private fun handleToggleEventTagSelection(
        state: TagSelectionState,
        intent: TagSelectionState.Intent.ToggleEventTagSelection,
    ) {
        val updatedTags = state.eventTagCategories.toggleTagSelection(intent.tag.id)

        setState(state = state.copy(eventTagCategories = updatedTags))
        eventTagStream.tryEmit(updatedTags)
    }

    private fun handleToggleAllEventTagsSelection(
        state: TagSelectionState,
        intent: TagSelectionState.Intent.ToggleAllEventTagsSelection,
    ) {
        val updatedTags = state.eventTagCategories.toggleCategorySelection(intent.category.id)

        setState(state = state.copy(eventTagCategories = updatedTags))
        eventTagStream.tryEmit(updatedTags)
    }

    suspend fun collectEventTagChangesAndSubmit() {
        eventTagStream
            .debounce(750)
            .distinctUntilChanged()
            .flatMapLatest { categories -> updateEventTagSelectionUseCase(categories) }
            .collectOutcome(
                onFailure = { sendUIEvent(UIEvent.SnackBar("Ausgewählte Themen konnten nicht gespeichert werden")) },
                onSuccess = { Log.d(javaClass.name, "Tags got updated") }
            )
    }

    private suspend fun collectArticleTagChangesAndSubmit() {
        articleTagStream
            .debounce(750)
            .distinctUntilChanged()
            .flatMapLatest { categories -> updateArticleTagSelectionUseCase(categories) }
            .collectOutcome(
                onFailure = { sendUIEvent(UIEvent.SnackBar("Ausgewählte Themen konnten nicht gespeichert werden")) },
                onSuccess = { Log.d(javaClass.name, "Tags got updated") }
            )
    }

    private fun List<TagCategory>.toggleTagSelection(tagId: Int): List<TagCategory> =
        map { tagCategory ->
            tagCategory.copy(
                tags = tagCategory.tags.map { tag ->
                    if (tag.id == tagId) tag.copy(isSelected = !tag.isSelected) else tag
                }
            )
        }

    private fun List<TagCategory>.toggleCategorySelection(categoryId: Int): List<TagCategory> =
        map { tagCategory ->
            if (tagCategory.id == categoryId) {
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
}