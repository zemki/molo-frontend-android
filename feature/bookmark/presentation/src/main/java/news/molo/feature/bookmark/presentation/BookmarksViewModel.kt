package news.molo.feature.bookmark.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.usecase.invoke
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import news.molo.core.navigation.Screen
import news.molo.core.util.paging.createPager
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.model.Article
import news.molo.domain.model.Event
import news.molo.domain.model.TabSelectionType
import news.molo.domain.usecase.article.GetPaginatedBookmarkedArticlesUseCase
import news.molo.domain.usecase.article.ObserveArticleBookmarksUseCase
import news.molo.domain.usecase.event.GetPaginatedBookmarkedEventsUseCase
import news.molo.domain.usecase.event.ObserveEventBookmarksUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarksViewModel(
    getPaginatedBookmarkedEventsUseCase: GetPaginatedBookmarkedEventsUseCase,
    observeEventBookmarksUseCase: ObserveEventBookmarksUseCase,
    getPaginatedBookmarkedArticlesUseCase: GetPaginatedBookmarkedArticlesUseCase,
    observeArticleBookmarksUseCase: ObserveArticleBookmarksUseCase
) : BaseViewModel<BookmarksState, BookmarksState.Intent>(BookmarksState()) {

    private val eventReloadTrigger = MutableSharedFlow<Unit>(replay = 1)
    private val articleReloadTrigger = MutableSharedFlow<Unit>(replay = 1)


    val eventPagingData: Flow<PagingData<Event>> = eventReloadTrigger
        .flatMapLatest {
            createPager(
                getItemsUseCase = getPaginatedBookmarkedEventsUseCase,
                paramsFactory = { pageSize, offset ->
                    GetPaginatedBookmarkedEventsUseCase.Params(
                        pageSize = pageSize,
                        offset = offset
                    )
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    val articlePagingData: Flow<PagingData<Article>> = articleReloadTrigger
        .flatMapLatest {
            createPager(
                getItemsUseCase = getPaginatedBookmarkedArticlesUseCase,
                paramsFactory = { pageSize, offset ->
                    GetPaginatedBookmarkedArticlesUseCase.Params(
                        pageSize = pageSize,
                        offset = offset
                    )
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    private val articleBookmarkObservation = observeArticleBookmarksUseCase()
        .onEach { reloadArticles() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val eventBookmarkObservation = observeEventBookmarksUseCase()
        .onEach { reloadEvents() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: BookmarksState.Intent) {
        when (intent) {
            is BookmarksState.Intent.SetSelectedTab -> handleSetSelectedTab(intent)
            is BookmarksState.Intent.ToArticleDetails -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(Screen.ArticleDetails(articleId = intent.articleId))
            )

            is BookmarksState.Intent.ToEventDetail -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(Screen.EventDetails(eventId = intent.eventId))
            )

            is BookmarksState.Intent.ToOrganisation -> dispatchNavigationEvent(
                NavigationEvent.NavigateTo(
                    Screen.OrganizationDetails(
                        organizationId = intent.organisationId, TabSelectionType.Events
                    )
                )
            )
        }
    }

    private fun handleSetSelectedTab(intent: BookmarksState.Intent.SetSelectedTab) {
        setState(currentState.copy(selectedTab = intent.tab))
    }

    private fun reloadEvents() {
        eventReloadTrigger.tryEmit(Unit)
    }

    private fun reloadArticles() {
        articleReloadTrigger.tryEmit(Unit)
    }
}