package news.molo.feature.info.presentation

import news.molo.domain.model.ColorSchemaPreference
import news.molo.feature.info.presentation.util.InfoMenuItem

data class InfoState(
    val isLoggedIn: Boolean = false,
    val colorSchemaPreference: ColorSchemaPreference = ColorSchemaPreference.System,
    val isDeleteAccountAlertVisible: Boolean = false,
    val infoMenuItems: List<InfoMenuItem> =  InfoMenuItem.allValues()
) {

    sealed interface Intent {
        data object DeleteAccount : Intent
        data object ShowDeleteAccountAlert : Intent
        data object DismissDeleteAccountAlert : Intent
        data object Logout : Intent
        data class SetColorSchema(val colorSchemaPreference: ColorSchemaPreference) : Intent
        data object ToOrganisationSummary: Intent
    }
}