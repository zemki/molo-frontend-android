package news.molo.feature.tags.presentation

import news.molo.domain.model.TabSelectionType
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory

data class TagSelectionState(
    val tabs: List<TabSelectionType> = TabSelectionType.entries,
    val selectedTab: TabSelectionType = TabSelectionType.Article,
    val tagConfigurationType: ConfigurationType = ConfigurationType.Default,
    val articleTagCategories: List<TagCategory> = emptyList(),
    val eventTagCategories: List<TagCategory> = emptyList()
) {

    sealed interface Intent {
        data class SetSelectedTab(val tab: TabSelectionType) : Intent
        data object NavigateUp: Intent

        data class ToggleArticleTagSelection(val tag: Tag) : Intent
        data class ToggleAllArticleTagsSelection(val category: TagCategory) : Intent

        data class ToggleEventTagSelection(val tag: Tag) : Intent
        data class ToggleAllEventTagsSelection(val category: TagCategory) : Intent
    }
}