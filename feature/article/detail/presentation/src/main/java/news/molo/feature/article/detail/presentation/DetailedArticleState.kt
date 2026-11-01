package news.molo.feature.article.detail.presentation

import news.molo.domain.model.Article

sealed interface DetailedArticleState {

    sealed interface Intent {
        data object NavigateUp : Intent
    }

    data object Loading : DetailedArticleState

    data class Loaded(val article: Article) : DetailedArticleState {
        sealed interface Intent : DetailedArticleState.Intent {
            data object ToggleBookmark : Intent
            data object Report : Intent
        }
    }
}