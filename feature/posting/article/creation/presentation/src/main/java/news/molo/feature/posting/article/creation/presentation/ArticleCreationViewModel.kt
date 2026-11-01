package news.molo.feature.posting.article.creation.presentation

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.ArticleCreationData
import news.molo.domain.model.withSelectedTags
import news.molo.domain.usecase.article.*

class ArticleCreationViewModel(
    private val articleId: Int? = null,
    private val generateArticleTagsUseCase: GenerateArticleTagsUseCase,
    private val createArticleUseCase: CreateArticleUseCase,
    private val updateArticleUseCase: UpdateArticleUseCase,
    private val getArticleDetailsWithTags: GetArticleUpdateDataUseCase,
    private val getArticleCreationDataUseCase: GetArticleCreationDataUseCase
) : BaseViewModel<ArticleCreationState, ArticleCreationState.Intent>(
    initialState = ArticleCreationState.Loading, useEventBus = false
) {

    init {
        if (articleId == null) {
            fetchDataForCreation()
        } else {
            fetchDataForUpdating(articleId)
        }
    }

    override fun executeIntent(intent: ArticleCreationState.Intent) {
        when (intent) {
            ArticleCreationState.Preview.Intent.DeleteImage -> updateDraft {
                it.copy(
                    imageUri = null, source = null
                )
            }

            is ArticleCreationState.Loaded.Intent.SetContent -> updateDraft { it.copy(content = intent.value) }
            is ArticleCreationState.Preview.Intent.SetImage -> updateDraft {
                it.copy(
                    imageUri = intent.uri, source = intent.source
                )
            }

            ArticleCreationState.Loaded.Intent.Cancel -> dispatchNavigationEvent(NavigationEvent.NavigateUp)

            is ArticleCreationState.Loaded.Intent.ToConfiguration -> handleIntent<_, _>(
                intent = intent, handler = ::handleToConfiguration
            )

            is ArticleCreationState.Preview.Intent.NavigateBack -> handleIntent<_, _>(
                intent = intent, handler = ::handleNavigateBackFromPreview
            )

            is ArticleCreationState.Loaded.Intent.SetHeadline -> updateDraft { it.copy(headline = intent.value) }

            is ArticleCreationState.Configuration.Intent.ToggleAllTagsSelection -> handleIntent<_, _>(
                intent = intent, handler = ::handleToggleAllTagsSelection
            )

            is ArticleCreationState.Configuration.Intent.ToggleTagSelection -> handleIntent<_, _>(
                intent = intent, handler = ::handleToggleTagSelection
            )

            ArticleCreationState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            is ArticleCreationState.Configuration.Intent.NavigateBack -> handleIntent<_, _>(
                intent = intent, handler = ::handleNavigateBackFromConfiguration
            )

            is ArticleCreationState.Configuration.Intent.SetLink -> updateDraft { it.copy(link = intent.value) }

            is ArticleCreationState.Configuration.Intent.ToPreview -> handleIntent<_, _>(
                intent = intent, handler = ::handleToToPreview
            )

            is ArticleCreationState.Intent.Post -> handleIntent<_, _>(
                intent = intent, handler = ::handlePost
            )
        }
    }

    private fun handleNavigateBackFromPreview(
        state: ArticleCreationState.Preview,
        intent: ArticleCreationState.Preview.Intent.NavigateBack
    ) {
        setState(ArticleCreationState.Configuration(draft = state.draft, original = state.original))
    }

    private fun handleNavigateBackFromConfiguration(
        state: ArticleCreationState.Configuration,
        intent: ArticleCreationState.Configuration.Intent.NavigateBack
    ) {
        setState(ArticleCreationState.Loaded(draft = state.draft, original = state.original))
    }

    private fun handleToToPreview(
        state: ArticleCreationState.Configuration,
        intent: ArticleCreationState.Configuration.Intent.ToPreview
    ) {
        setState(ArticleCreationState.Preview(draft = state.draft, original = state.original))
    }

    private fun handleToggleTagSelection(
        state: ArticleCreationState.Configuration,
        intent: ArticleCreationState.Configuration.Intent.ToggleTagSelection,
    ) {
        val draft = state.draft.copy(
            tagCategories = state.draft.tagCategories.withSelectedTags { tag ->
                if (tag.id == intent.tag.id) !tag.isSelected else tag.isSelected
            })

        setState(state.copy(draft = draft))
    }

    private fun handleToggleAllTagsSelection(
        state: ArticleCreationState.Configuration,
        intent: ArticleCreationState.Configuration.Intent.ToggleAllTagsSelection,
    ) {

        val tags = state.draft.tagCategories.map { tagCategory ->
            if (tagCategory.id == intent.category.id) {
                val shouldSelectAll = tagCategory.tags.any { !it.isSelected }

                tagCategory.copy(
                    tags = tagCategory.tags.map { tag ->
                        tag.copy(isSelected = shouldSelectAll)
                    })
            } else {
                tagCategory
            }
        }

        setState(state.copy(draft = state.draft.copy(tagCategories = tags)))
    }

    private fun handleToConfiguration(
        state: ArticleCreationState.Loaded,
        intent: ArticleCreationState.Loaded.Intent.ToConfiguration
    ) {

        val current = state.draft

        viewModelScope.launch {
            when (current.headline.isNotBlank() && current.content.isNotBlank()) {
                true -> generateArticleTagsUseCase(
                    GenerateArticleTagsUseCase.Params(
                        title = current.headline, content = current.content
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
                        ArticleCreationState.Configuration(
                            draft = current.copy(
                                tagCategories = preselectedTags
                            ), original = state.original
                        )
                    )
                }, onProgress = { setState(ArticleCreationState.Loading) })

                false -> setState(
                    ArticleCreationState.Configuration(
                        draft = current, original = state.original
                    )
                )
            }
        }
    }

    private fun handlePost(
        state: ArticleCreationState,
        intent: ArticleCreationState.Intent.Post
    ) {

        val draft = state.draft ?: run {
            sendUIEvent(UIEvent.SnackBar("Das sollte nicht passieren. Bitte melde diesen Fehler."))
            return
        }

        viewModelScope.launch {
            if (articleId == null) {
                createArticleUseCase(
                    CreateArticleUseCase.Params(
                        article = ArticleCreationData(
                            id = -1,
                            title = draft.headline,
                            content = draft.content,
                            tags = draft.tagCategories.flatMap { it.tags.filter { it.isSelected } },
                            asDraft = intent.asDraft,
                            link = draft.link
                        ),
                        pictureUrl = draft.imageUri.takeUnless { it?.scheme == "https" }
                            ?.toString(),
                        pictureSource = draft.source.orEmpty()
                    )
                )
            } else {
                updateArticleUseCase(
                    UpdateArticleUseCase.Params(
                        article = ArticleCreationData(
                            id = articleId,
                            title = draft.headline,
                            content = draft.content,
                            tags = draft.tagCategories.flatMap { it.tags.filter { it.isSelected } },
                            asDraft = intent.asDraft,
                            link = draft.link
                        ),
                        pictureUrl = draft.imageUri.takeUnless { it?.scheme == "https" }
                            ?.toString(),
                        pictureSource = draft.source.orEmpty()
                    )
                )
            }.collectOutcome(onSuccess = {
                val successState = if (articleId == null) {
                    ArticleCreationState.Created
                } else {
                    ArticleCreationState.Updated
                }

                setState(successState)
            }, onProgress = { setState(ArticleCreationState.Loading) }, onFailure = {
                setState(state)
                sendUIEvent(UIEvent.SnackBar(it.message))
            })
        }
    }

    private fun updateDraft(reducer: (ArticleDraft) -> ArticleDraft) {
        val currentDraft = when (val s = currentState) {
            is ArticleCreationState.Loaded -> s.draft
            is ArticleCreationState.Configuration -> s.draft
            is ArticleCreationState.Preview -> s.draft
            else -> return
        }

        val newDraft = reducer(currentDraft)

        val newState = when (val s = currentState) {
            is ArticleCreationState.Loaded -> s.copy(draft = newDraft)
            is ArticleCreationState.Configuration -> s.copy(draft = newDraft)
            is ArticleCreationState.Preview -> s.copy(draft = newDraft)
            else -> s
        }
        setState(newState)
    }

    private fun fetchDataForCreation() {
        viewModelScope.launch {
            getArticleCreationDataUseCase().collectOutcome(
                onSuccess = { (user, tags) ->
                    val draft = ArticleDraft(
                        userAvatarUrl = user.imageUrl,
                        userDisplayName = user.displayName,
                        tagCategories = tags,
                        source = user.displayName
                    )
                    setState(
                        ArticleCreationState.Loaded(
                            draft = draft, original = draft
                        )
                    )
                })
        }
    }

    private fun fetchDataForUpdating(articleId: Int) {
        viewModelScope.launch {
            getArticleDetailsWithTags(articleId).collectOutcome(
                onSuccess = { (article, tags) ->
                    val draft = ArticleDraft(
                        userAvatarUrl = article.organization.imageURL,
                        userDisplayName = article.organization.name,
                        tagCategories = tags,
                        headline = article.title,
                        content = article.content,
                        imageUri = article.imageURL.takeUnless { it.isEmpty() }?.toUri(),
                        source = article.imageSource,
                        link = article.link
                    )
                    setState(
                        ArticleCreationState.Loaded(
                            draft = draft, original = draft
                        )
                    )
                }
            )
        }
    }
}


