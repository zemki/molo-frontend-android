package news.molo.android.presentation

import news.molo.domain.model.ColorSchemaPreference
import news.molo.domain.model.SearchHistoryEntry
import news.molo.domain.model.SearchHistoryType
import news.molo.domain.model.SelfUserData
import news.molo.domain.model.TabSelectionType

data class AppState(
    val onboardingCompletionState: OnboardingCompletionState? = null,
    val selectedLocation: String = "",
    val colorSchemaPreference: ColorSchemaPreference? = null,
    val bottomSheetState: BottomSheetState = BottomSheetState.Hidden,
    val isLoggedIn: Boolean = false,
    val selfUserData: SelfUserData? = null,
    val searchHistory: List<SearchHistoryEntry> = emptyList(),
    val query: String = ""
) {
    sealed interface Intent {
        data object Logout : Intent
        data object ShowLogin : Intent
        data object HideCurrentSheet : Intent
        data object ToArticlePostCreation : Intent
        data object ToEventPostCreation : Intent
        data class SetBearer(val bearer: String) : Intent
        data class NavigateToEvent(val eventId: Int) : Intent
        data class NavigateToArticle(val articleId: Int) : Intent
        data class NavigateToSearchResults(
            val query: String,
            val type: SearchHistoryType,
            val selectedTab: TabSelectionType
        ) : Intent

        data class DeleteSearchHistoryEntry(val id: Int) : Intent
        data object DeleteSearchHistory : Intent
        data class SetQuery(val query: String) : Intent

    }
}

enum class OnboardingCompletionState {
    Complete,
    NotComplete,
}
