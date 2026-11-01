package news.molo.feature.feed.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import de.convelop.forge.navigation.NavigationEvent.NavigateTo
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import news.molo.core.navigation.Screen.ArticleDetails
import news.molo.core.navigation.Screen.OrganizationDetails
import news.molo.core.util.paging.createPager
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.area.ObserveAreaAndRadiusSelectionUseCase
import news.molo.domain.usecase.article.GetPaginatedArticlesWithTags
import news.molo.domain.usecase.posting.ObservePostingReloadTriggerUseCase
import news.molo.domain.usecase.tag.ObserveTagSelectionUseCase

class FeedViewModel(
    getPaginatedArticlesWithTags: GetPaginatedArticlesWithTags,
    observeAreaAndRadiusSelectionUseCase: ObserveAreaAndRadiusSelectionUseCase,
    observeTagSelectionUseCase: ObserveTagSelectionUseCase,
    observePostingReloadTriggerUseCase: ObservePostingReloadTriggerUseCase
) : BaseViewModel<FeedState, FeedState.Intent>(FeedState()) {

    private val searchQueryFlow = MutableStateFlow("")
    private val refreshTrigger = MutableSharedFlow<Unit>(
        replay = 0, extraBufferCapacity = 1
    )

    private val filterFlow = MutableSharedFlow<ArticleFilter?>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val articles = combine(
        merge(
            searchQueryFlow.debounce(500).distinctUntilChanged(),
            observeAreaAndRadiusSelectionUseCase().map { searchQueryFlow.value },
            observeTagSelectionUseCase().map { searchQueryFlow.value },
            observePostingReloadTriggerUseCase().map { searchQueryFlow.value },
            refreshTrigger.map { searchQueryFlow.value },
        ), filterFlow.onStart { emit(null) }) { query, filter ->
        query to filter
    }.flatMapLatest { (query, filter) ->
        createPager(
            getItemsUseCase = getPaginatedArticlesWithTags, paramsFactory = { pageSize, offset ->
                GetPaginatedArticlesWithTags.Params(
                    pageSize = pageSize,
                    offset = offset,
                    query = query,
                    includeMedia = filter is ArticleFilter.Media || filter == null,
                    includeOfficial = filter is ArticleFilter.Official || filter == null,
                    includeCommunity = filter is ArticleFilter.Community || filter == null
                )
            }).flow.cachedIn(viewModelScope)
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = PagingData.empty()
    )

    override fun executeIntent(intent: FeedState.Intent) {
        when (intent) {
            FeedState.Intent.Refresh -> refreshTrigger.tryEmit(Unit)
            is FeedState.Intent.Query -> {
                setState(currentState.copy(query = intent.query))
                searchQueryFlow.tryEmit(intent.query)
            }

            is FeedState.Intent.ToArticleDetails -> dispatchNavigationEvent(
                NavigateTo(
                    ArticleDetails(
                        intent.articleId
                    )
                )
            )

            is FeedState.Intent.ToOrganisation -> dispatchNavigationEvent(
                NavigateTo(
                    OrganizationDetails(
                        organizationId = intent.organisationId,
                        defaultTagSelectionType = TabSelectionType.Article,
                    )
                )
            )

            is FeedState.Intent.SelectFilter -> handleIntent<_, _>(
                intent = intent, handler = ::handleSelectFilter
            )
        }
    }

    private fun handleSelectFilter(
        state: FeedState,
        intent: FeedState.Intent.SelectFilter,
    ) {
        
        val currentFilter = state.selectedFilter

        if (intent.filter == currentFilter) {
            setState(state.copy(selectedFilter = null))
            filterFlow.tryEmit(null)
            return
        }

        setState(state.copy(selectedFilter = intent.filter))
        filterFlow.tryEmit(intent.filter)
    }
}