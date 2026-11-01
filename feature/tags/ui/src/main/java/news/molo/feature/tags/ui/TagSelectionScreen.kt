package news.molo.feature.tags.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.molecule.ContinueFab
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.TabSelectionType
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory
import news.molo.feature.tags.presentation.TagSelectionState
import news.molo.feature.tags.presentation.TagSelectionViewModel
import news.molo.feature.tags.ui.page.TagCategoryFlowRowPage
import news.molo.feature.tags.ui.page.TagSelectionPage

@Composable
fun TagSelectionScreen(
    viewModel: TagSelectionViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    TagSelectionScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagSelectionScreenImpl(
    state: TagSelectionState,
    executeIntent: (TagSelectionState.Intent) -> Unit
) {

    val isOnboarding = state.tagConfigurationType == ConfigurationType.Onboarding

    // We don't want to allow back navigation in onboarding mode
    BackHandler(isOnboarding) { }

    ScreenScaffold(
        topBar = {
            TagTopAppBar(
                tagConfigurationType = state.tagConfigurationType,
                onNavigateUpClick = { executeIntent(TagSelectionState.Intent.NavigateUp) }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AnimatedVisibility(isOnboarding) {
                ContinueFab(
                    text = "Weiter",
                    onClick = {executeIntent(TagSelectionState.Intent.NavigateUp)}
                )
            }
        }
    ) {
        TagSelectionPage(
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            onTabSelect = { executeIntent(TagSelectionState.Intent.SetSelectedTab(it)) },
            tabContent = { selectedTab ->
                TagTabContent(
                    selectedTab = selectedTab,
                    articleTagCategories = state.articleTagCategories,
                    eventTagCategories = state.eventTagCategories,
                    onArticleTagClick = { executeIntent(TagSelectionState.Intent.ToggleArticleTagSelection(it))},
                    onToggleAllArticleTagsInCategoryClick = { executeIntent(TagSelectionState.Intent.ToggleAllArticleTagsSelection(it))},
                    onEventTagClick = { executeIntent(TagSelectionState.Intent.ToggleEventTagSelection(it)) },
                    onToggleAllEventTagsInCategoryClick = { executeIntent(TagSelectionState.Intent.ToggleAllEventTagsSelection(it)) }
                )
            }
        )
    }
}

@Composable
private fun TagTopAppBar(tagConfigurationType: ConfigurationType, onNavigateUpClick: () -> Unit) {
    when (tagConfigurationType) {
        ConfigurationType.Default -> DefaultTopAppBar(
            title = stringResource(R.string.title_tags),
            onNavigateBackClick = onNavigateUpClick
        )

        ConfigurationType.Onboarding -> {}
        ConfigurationType.EventCreation -> {}
    }
}

@Composable
private fun TagTabContent(
    selectedTab: TabSelectionType,
    articleTagCategories: List<TagCategory>,
    eventTagCategories: List<TagCategory>,
    onArticleTagClick: (Tag) -> Unit,
    onToggleAllArticleTagsInCategoryClick: (TagCategory) -> Unit,
    onEventTagClick: (Tag) -> Unit,
    onToggleAllEventTagsInCategoryClick: (TagCategory) -> Unit,
) {
    when (selectedTab) {
        TabSelectionType.Article -> TagCategoryFlowRowPage(
            tagCategories = articleTagCategories,
            onTagClick = onArticleTagClick,
            onToggleAllInCategoryClick = onToggleAllArticleTagsInCategoryClick
        )

        TabSelectionType.Events -> TagCategoryFlowRowPage(
            tagCategories = eventTagCategories,
            onTagClick = onEventTagClick,
            onToggleAllInCategoryClick = onToggleAllEventTagsInCategoryClick
        )
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        TagSelectionScreenImpl(
            state = TagSelectionState(),
            executeIntent = {}
        )
    }
}