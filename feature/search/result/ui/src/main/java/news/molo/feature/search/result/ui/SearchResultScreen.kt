package news.molo.feature.search.result.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.molecule.SearchField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Article
import news.molo.domain.model.Event
import news.molo.feature.search.result.presentation.SearchResultState
import news.molo.feature.search.result.presentation.SearchResultType
import news.molo.feature.search.result.presentation.SearchResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    viewModel: SearchResultViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    SearchResultScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent,
        articlePagingData = viewModel.articlePagingData,
        eventPagingData = viewModel.eventPagingData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultScreenImpl(
    state: SearchResultState,
    executeIntent: (SearchResultState.Intent) -> Unit,
    articlePagingData: Flow<PagingData<Article>>,
    eventPagingData: Flow<PagingData<Event>>
) {

    ScreenScaffold(topBar = {
        DefaultTopAppBar(
            onNavigateBackClicked = { executeIntent(SearchResultState.Intent.NavigateBack) },
            title = {
                SearchField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.Medium),
                    query = state.query,
                    onQueryChanged = { executeIntent(SearchResultState.Intent.SetQuery(it)) }
                )
            }
        )
    }) {
        SearchResultPage(
            query = state.query,
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            onQueryChange = { executeIntent(SearchResultState.Intent.SetQuery(it)) },
            onTabSelectionChange = { executeIntent(SearchResultState.Intent.SetSelectedTab(it)) },
            tabContent = { selectedTab ->
                when (selectedTab) {
                    SearchResultType.Article -> ArticleSearchResultPage(
                        articlePagingDataFlow = articlePagingData,
                        onArticleClick = {
                            executeIntent(
                                SearchResultState.Intent.ToArticleDetails(
                                    it
                                )
                            )
                        },
                        onOrganizationClick = {
                            executeIntent(
                                SearchResultState.Intent.ToOrganisation(
                                    it
                                )
                            )
                        },
                        onRefresh = {
                            executeIntent(SearchResultState.Intent.RefreshArticles)
                        }
                    )

                    SearchResultType.Events -> EventsSearchResultPage(
                        eventPagingDataFlow = eventPagingData,
                        onEventClick = { executeIntent(SearchResultState.Intent.ToEventDetail(it)) },
                        onOrganizationClick = {
                            executeIntent(
                                SearchResultState.Intent.ToOrganisation(
                                    it
                                )
                            )
                        },
                        onRefresh = {
                            executeIntent(SearchResultState.Intent.RefreshEvents)
                        }
                    )
                }
            },
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = SearchResultState(query = "")

    ComponentPreviewTemplate {
        SearchResultScreenImpl(
            state = state,
            executeIntent = {},
            articlePagingData = emptyFlow(),
            eventPagingData = emptyFlow(),
        )
    }
}