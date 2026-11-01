package news.molo.core.navigation

import kotlinx.serialization.Serializable
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.TabSelectionType

@Serializable
sealed interface Screen {

    @Serializable
    data object Graph : Screen

    @Serializable
    object PasswordResetGraph

    @Serializable
    data class ArticleDetails(val articleId: Int) : Screen

    @Serializable
    data class EventDetails(val eventId: Int) : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object PostingType : Screen

    @Serializable
    data object OrganizationSummary : Screen

    @Serializable
    data object RequestPasswordReset : Screen

    @Serializable
    data class PasswordReset(val token: String = "") : Screen

    @Serializable
    data class OrganizationDetails(
        val organizationId: Int,
        val defaultTagSelectionType: TabSelectionType,
    ) : Screen

    @Serializable
    data object MyPosts : Screen

    @Serializable
    data object AreaSelection : Screen

    @Serializable
    data class TagSelection(
        val defaultTagSelectionType: TabSelectionType,
        val tagConfigurationType: ConfigurationType = ConfigurationType.Default
    ) : Screen

    @Serializable
    data class LocationSelection(
        val configurationType: ConfigurationType,
        val initialLatitude: Double? = null,
        val initialLongitude: Double? = null
    ) : Screen

    @Serializable
    data class ArticleCreation(val articleId: Int? = null) : Screen

    @Serializable
    data class EventCreation(val eventId: Int? = null) : Screen

    @Serializable
    data object Registration : Screen

    @Serializable
    data class SearchResults(val query: String, val initialTabSelectionType: TabSelectionType) :
        Screen
}