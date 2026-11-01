package news.molo.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface TopLevelScreen : Screen {

    @Serializable

    data class Graph(val bearer: String = "") : TopLevelScreen

    @Serializable
    data object Events : TopLevelScreen

    @Serializable
    data object Feed : TopLevelScreen

    @Serializable
    data object Bookmarks : TopLevelScreen

    @Serializable
    data object Infos : TopLevelScreen
}