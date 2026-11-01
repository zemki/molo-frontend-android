package news.molo.feature.organization.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.domain.model.Article
import news.molo.domain.model.Event
import news.molo.domain.model.TabSelectionType
import news.molo.feature.organization.presentation.DetailedOrganizationState
import news.molo.feature.organization.presentation.DetailedOrganizationViewModel
import news.molo.feature.organization.ui.page.DetailedOrganizationPage
import news.molo.feature.organization.ui.page.OrganizationArticlePage
import news.molo.feature.organization.ui.page.OrganizationEventPage

@Composable
fun DetailedOrganizationScreen(
    viewModel: DetailedOrganizationViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    DetailedOrganizationScreenImpl(
        uiEvents = viewModel.events,
        state = state,
        executeIntent = viewModel::executeIntent,
        articlePagingData = viewModel.articlePagingData,
        eventPagingData = viewModel.eventPagingData
    )
}

@Composable
private fun DetailedOrganizationScreenImpl(
    state: DetailedOrganizationState,
    uiEvents: SharedFlow<UIEvent>,
    articlePagingData: Flow<PagingData<Article>>,
    eventPagingData: Flow<PagingData<Event>>,
    executeIntent: (DetailedOrganizationState.Intent) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }

    UIEventHandler(
        uiEvent = uiEvents,
        snackBarHostState = snackBarHostState,
        onEventHandled = { executeIntent(DetailedOrganizationState.Intent.NavigateUp) }
    )

    when (state) {
        is DetailedOrganizationState.Loaded -> ScreenScaffold(
            topBar = {
                DetailedOrganizationTopBar(
                    isProfileActionVisible = state.organization.isOwnedBySelfUser,
                    onProfileClick = { executeIntent(DetailedOrganizationState.Loaded.Intent.ToProfile) },
                    onNavigateUpClick = { executeIntent(DetailedOrganizationState.Intent.NavigateUp) }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) {
            DetailedOrganizationPage(
                tabs = state.tabs,
                selectedTab = state.selectedTab,
                organizationLogoUrl = state.organization.imageURL,
                organizationName = state.organization.name,
                organizationDescription = state.organization.description,
                tabContent = { tabSelection, listState ->
                    OrganizationTabRouter(
                        tabSelection = tabSelection,
                        listState = listState,
                        isOwnedBySelf = state.organization.isOwnedBySelfUser,
                        articlePagingData = articlePagingData,
                        eventPagingData = eventPagingData,
                        onArticleClick = {
                            executeIntent(
                                DetailedOrganizationState.Loaded.Intent.ToArticle(
                                    it
                                )
                            )
                        },
                        onDeleteArticleClick = {
                            executeIntent(
                                DetailedOrganizationState.Loaded.Intent.DeleteArticle(
                                    it
                                )
                            )
                        },
                        onEventClick = {
                            executeIntent(
                                DetailedOrganizationState.Loaded.Intent.ToEvent(
                                    it
                                )
                            )
                        },
                        onDeleteEventClick = {
                            executeIntent(
                                DetailedOrganizationState.Loaded.Intent.DeleteEvent(
                                    it
                                )
                            )
                        },
                    )
                },
                titleForType = state::titleForType
            )
        }

        DetailedOrganizationState.Loading -> CoveringProgressIndicator()

    }
}

@Composable
private fun OrganizationTabRouter(
    tabSelection: TabSelectionType,
    listState: LazyListState,
    isOwnedBySelf: Boolean,
    articlePagingData: Flow<PagingData<Article>>,
    eventPagingData: Flow<PagingData<Event>>,
    onArticleClick: (Int) -> Unit,
    onDeleteArticleClick: (Int) -> Unit,
    onEventClick: (Int) -> Unit,
    onDeleteEventClick: (Int) -> Unit
) {
    when (tabSelection) {
        TabSelectionType.Article -> OrganizationArticlePage(
            pagingDataFlow = articlePagingData,
            listState = listState,
            areArticleDeletable = isOwnedBySelf,
            onArticleClick = onArticleClick,
            onDeleteClick = onDeleteArticleClick
        )

        TabSelectionType.Events -> OrganizationEventPage(
            pagingDataFlow = eventPagingData,
            listState = listState,
            areEventsDeletable = isOwnedBySelf,
            onEventClick = onEventClick,
            onDeleteClick = onDeleteEventClick
        )
    }
}

@Composable
private fun DetailedOrganizationTopBar(
    isProfileActionVisible: Boolean,
    onProfileClick: () -> Unit,
    onNavigateUpClick: () -> Unit,
) {
    DefaultTopAppBar(
        onNavigateBackClick = onNavigateUpClick,
        actions = {
            if (isProfileActionVisible) {
                IconButton(onClick = onProfileClick) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }
        }
    )
}
