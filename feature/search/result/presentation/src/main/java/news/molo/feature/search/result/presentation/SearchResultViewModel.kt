package news.molo.feature.search.result.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.navigation.NavigationEvent.NavigateTo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import news.molo.core.navigation.Screen.ArticleDetails
import news.molo.core.navigation.Screen.EventDetails
import news.molo.core.navigation.Screen.OrganizationDetails
import news.molo.core.util.paging.createPager
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.article.GetPaginatedArticlesWithQueryUseCase
import news.molo.domain.usecase.event.GetPaginatedEventsWithQueryUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class SearchResultViewModel(
    query: String,
    initialTab: TabSelectionType,
    getPaginatedArticlesWithQueryUseCase: GetPaginatedArticlesWithQueryUseCase,
    getPaginatedEventsWithQueryUseCase: GetPaginatedEventsWithQueryUseCase
) : BaseViewModel<SearchResultState, SearchResultState.Intent>(
    SearchResultState(
        query = query,
        selectedTab = when (initialTab) {
            TabSelectionType.Article -> SearchResultType.Article
            TabSelectionType.Events -> SearchResultType.Events
        }
    )
) {

    private val eventReloadTrigger = MutableSharedFlow<Unit>(replay = 1)
    private val articleReloadTrigger = MutableSharedFlow<Unit>(replay = 1)

    private val searchQueryFlow = MutableStateFlow(query)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val articlePagingData = merge(
        searchQueryFlow.debounce(500).distinctUntilChanged(),
        articleReloadTrigger.map { searchQueryFlow.value },
    ).flatMapLatest { query ->
        createPager(
            getItemsUseCase = getPaginatedArticlesWithQueryUseCase,
            paramsFactory = { pageSize, offset ->
                GetPaginatedArticlesWithQueryUseCase.Params(
                    pageSize = pageSize,
                    offset = offset,
                    query = query
                )
            }
        ).flow.cachedIn(viewModelScope)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PagingData.empty()
    )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val eventPagingData = merge(
        searchQueryFlow.debounce(500).distinctUntilChanged(),
        eventReloadTrigger.map { searchQueryFlow.value },
    ).flatMapLatest { query ->
        createPager(
            getItemsUseCase = getPaginatedEventsWithQueryUseCase,
            paramsFactory = { pageSize, offset ->
                GetPaginatedEventsWithQueryUseCase.Params(
                    pageSize = pageSize,
                    offset = offset,
                    query = query
                )
            }
        ).flow.cachedIn(viewModelScope)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PagingData.empty()
    )

    override fun executeIntent(intent: SearchResultState.Intent) {
        when (intent) {
            is SearchResultState.Intent.SetSelectedTab -> handleSetSelectedTab(intent)
            is SearchResultState.Intent.ToArticleDetails -> dispatchNavigationEvent(
                NavigateTo(ArticleDetails(articleId = intent.articleId))
            )

            is SearchResultState.Intent.ToEventDetail -> dispatchNavigationEvent(
                NavigateTo(EventDetails(eventId = intent.eventId))
            )

            is SearchResultState.Intent.ToOrganisation -> dispatchNavigationEvent(
                NavigateTo(
                    OrganizationDetails(
                        organizationId = intent.organisationId, TabSelectionType.Events
                    )
                )
            )

            is SearchResultState.Intent.SetQuery -> {
                setState(currentState.copy(query = intent.query))
                searchQueryFlow.tryEmit(intent.query)
            }

            SearchResultState.Intent.NavigateBack -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
            SearchResultState.Intent.RefreshArticles -> {
                articleReloadTrigger.tryEmit(Unit)
            }

            SearchResultState.Intent.RefreshEvents -> {
                eventReloadTrigger.tryEmit(Unit)
            }
        }
    }

    private fun handleSetSelectedTab(intent: SearchResultState.Intent.SetSelectedTab) {
        setState(currentState.copy(selectedTab = intent.tab))
    }
}