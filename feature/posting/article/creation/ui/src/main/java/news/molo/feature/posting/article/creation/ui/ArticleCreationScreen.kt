package news.molo.feature.posting.article.creation.ui

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
import news.molo.feature.posting.article.creation.presentation.ArticleCreationState
import news.molo.feature.posting.article.creation.presentation.ArticleCreationState.Loaded.Intent.SetContent
import news.molo.feature.posting.article.creation.presentation.ArticleCreationState.Loaded.Intent.SetHeadline
import news.molo.feature.posting.article.creation.presentation.ArticleCreationState.Preview.Intent.NavigateBack
import news.molo.feature.posting.article.creation.presentation.ArticleCreationState.Preview.Intent.SetImage
import news.molo.feature.posting.article.creation.presentation.ArticleCreationViewModel
import news.molo.feature.posting.article.creation.presentation.ArticleDraft

@Composable
fun ArticleCreationScreen(
    viewModel: ArticleCreationViewModel
) {

    val state by viewModel.collectStateFlow().collectAsState()

    ArticleCreationScreenImpl(
        state = state,
        events = viewModel.events,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleCreationScreenImpl(
    state: ArticleCreationState,
    events: Flow<UIEvent>,
    executeIntent: (ArticleCreationState.Intent) -> Unit
) {

    var showUnsavedChangesAlert by remember { mutableStateOf(false) }
    var showMissingFieldsAlert by remember { mutableStateOf(false) }

    BackHandler(state.hasChanges) {
        showUnsavedChangesAlert = true
    }
    BackHandler(state is ArticleCreationState.Preview) {
        executeIntent(NavigateBack)
    }
    BackHandler(state is ArticleCreationState.Configuration) {
        executeIntent(ArticleCreationState.Configuration.Intent.NavigateBack)
    }

    val snackBarHostState = remember { SnackbarHostState() }

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
                executeIntent(ArticleCreationState.Intent.Post(asDraft = true))
            },
            onDiscard = {
                showUnsavedChangesAlert = false
                executeIntent(ArticleCreationState.Intent.NavigateUp)
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
            ArticleCreationTopBar(
                state = state,
                executeIntent = executeIntent,
                onShowUnsaveChangesAlert = { showUnsavedChangesAlert = true },
                onShowMissingFieldsAlert = { showMissingFieldsAlert = true }
            )
        }
    ) {
        when (state) {
            ArticleCreationState.Loading -> CoveringProgressIndicator()

            is ArticleCreationState.Loaded -> ArticleCreationPage(
                headline = state.draft.headline,
                content = state.draft.content,
                profilePictureUrl = state.draft.userAvatarUrl,
                onHeadlineChange = { executeIntent(SetHeadline(it)) },
                onContentChange = { executeIntent(SetContent(it)) },
            )

            is ArticleCreationState.Configuration -> ArticleConfigurationPage(
                link = state.draft.link,
                tagCategories = state.draft.tagCategories,
                onSelectTag = {
                    executeIntent(ArticleCreationState.Configuration.Intent.ToggleTagSelection(it))
                },
                onSelectCategory = {
                    executeIntent(
                        ArticleCreationState.Configuration.Intent.ToggleAllTagsSelection(it)
                    )
                },
                onLinkChange = { executeIntent(ArticleCreationState.Configuration.Intent.SetLink(it)) }
            )

            is ArticleCreationState.Preview -> ArticlePreviewPage(
                headline = state.draft.headline,
                content = state.draft.content,
                profilePictureUrl = state.draft.userAvatarUrl,
                imageUri = state.draft.imageUri,
                imageSource = state.draft.source.orEmpty(),
                onSelectImage = { uri, source ->
                    executeIntent(SetImage(uri = uri, source = source))
                },
                displayName = state.draft.userDisplayName,
                onClickPost = { executeIntent(ArticleCreationState.Intent.Post()) },
                onClickSaveDraft = { executeIntent(ArticleCreationState.Intent.Post(asDraft = true)) },
            )

            ArticleCreationState.Created -> ArticleUpdatedPage(
                headline = stringResource(R.string.article_posting_created_title),
                text = stringResource(R.string.article_posting_created_description),
                onClickNext = { executeIntent(ArticleCreationState.Intent.NavigateUp) }
            )

            ArticleCreationState.DraftCreated -> ArticleUpdatedPage(
                headline = stringResource(R.string.article_posting_created_title),
                text = stringResource(R.string.article_posting_created_description),
                onClickNext = { executeIntent(ArticleCreationState.Intent.NavigateUp) }
            )

            ArticleCreationState.Updated -> ArticleUpdatedPage(
                headline = stringResource(R.string.article_posting_updated_title),
                text = stringResource(R.string.article_posting_updated_description),
                onClickNext = { executeIntent(ArticleCreationState.Intent.NavigateUp) }
            )
        }
    }
}

@Composable
private fun ArticleCreationTopBar(
    state: ArticleCreationState,
    executeIntent: (ArticleCreationState.Intent) -> Unit,
    onShowUnsaveChangesAlert: () -> Unit,
    onShowMissingFieldsAlert: () -> Unit
) {
    when (state) {
        is ArticleCreationState.Loaded -> CreationTopBar(
            onBackClick = {
                when (state.hasChanges) {
                    true -> onShowUnsaveChangesAlert()
                    false -> executeIntent(ArticleCreationState.Loaded.Intent.Cancel)
                }
            },
            onNextClick = {
                when (state.missingFields.isEmpty()) {
                    true -> executeIntent(ArticleCreationState.Loaded.Intent.ToConfiguration)
                    false -> onShowMissingFieldsAlert()
                }
            }
        )

        is ArticleCreationState.Preview -> DefaultTopAppBar(
            onNavigateBackClick = { executeIntent(NavigateBack) }
        )

        is ArticleCreationState.Configuration -> CreationTopBar(
            onBackClick = { executeIntent(ArticleCreationState.Configuration.Intent.NavigateBack) },
            onNextClick = {
                when (state.missingFields.isEmpty()) {
                    true -> executeIntent(ArticleCreationState.Configuration.Intent.ToPreview)
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
        ArticleCreationScreenImpl(
            state = ArticleCreationState.Loaded(draft = ArticleDraft(), original = ArticleDraft()),
            events = emptyFlow(),
            executeIntent = { }
        )
    }
}