package news.molo.feature.article.detail.presentation

import androidx.lifecycle.viewModelScope
import de.convelop.forge.navigation.NavigationEvent
import de.convelop.forge.outcome.collectOutcome
import de.convelop.forge.outcome.onEachOutcome
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import news.molo.core.event.UIEvent
import news.molo.core.viewmodel.BaseViewModel
import news.molo.domain.usecase.article.BookmarkArticleUseCase
import news.molo.domain.usecase.article.FlagArticleUseCase
import news.molo.domain.usecase.article.GetArticleDetailsUseCase
import news.molo.domain.usecase.article.RemoveArticleBookmarkUseCase

class DetailedArticleViewModel(
    private val articleId: Int,
    getArtDetailsUseCase: GetArticleDetailsUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase,
    private val removeArticleBookmarkUseCase: RemoveArticleBookmarkUseCase,
    private val flagArticleUseCase: FlagArticleUseCase
) : BaseViewModel<DetailedArticleState, DetailedArticleState.Intent>(
    DetailedArticleState.Loading,
    useEventBus = false
) {

    private val articleDetails = getArtDetailsUseCase(articleId)
        .onEachOutcome(
            onProgress = { setState(DetailedArticleState.Loading) },
            onFailure = { dispatchNavigationEvent(NavigationEvent.NavigateUp) },
            onSuccess = { setState(DetailedArticleState.Loaded(it)) }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = null
        )

    override fun executeIntent(intent: DetailedArticleState.Intent) {
        when (intent) {
            is DetailedArticleState.Loaded.Intent.ToggleBookmark -> handleIntent<_, _>(
                intent,
                ::handleToggleBookmark
            )

            is DetailedArticleState.Loaded.Intent.Report -> handleIntent<_, _>(
                intent,
                ::handleReportIntent
            )

            DetailedArticleState.Intent.NavigateUp -> dispatchNavigationEvent(NavigationEvent.NavigateUp)
        }
    }

    private fun handleToggleBookmark(
        state: DetailedArticleState.Loaded,
        intent: DetailedArticleState.Loaded.Intent.ToggleBookmark
    ) {
        viewModelScope.launch {
            when (state.article.isBookmarked) {
                true -> removeArticleBookmarkUseCase(articleId)
                false -> bookmarkArticleUseCase(articleId)
            }.collectOutcome(
                onProgress = { setState(DetailedArticleState.Loading) },
                onFailure = {
                    showSnackbar(it.message)
                    setState(state)
                },
                onSuccess = { setState(state.copy(article = state.article.copy(isBookmarked = !state.article.isBookmarked))) }
            )
        }
    }

    private fun handleReportIntent(
        state: DetailedArticleState.Loaded,
        intent: DetailedArticleState.Loaded.Intent.Report
    ) {
        viewModelScope.launch {
            flagArticleUseCase(articleId)
                .collectOutcome(
                    onFailure = { showSnackbar(it.message) },
                    onSuccess = { sendUIEvent(UIEvent.SnackBar("Artikel wurde gemeldet.")) }
                )
        }
    }
}