package news.molo.android.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import news.molo.core.navigation.TopLevelScreen
import news.molo.feature.bookmark.ui.BookmarksScreen
import news.molo.feature.event.ui.EventsScreen
import news.molo.feature.feed.ui.FeedScreen
import news.molo.feature.info.ui.InfoScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.topLevelGraph(onRequestLogin: () -> Unit) {

    composable<TopLevelScreen.Events> {
        EventsScreen(viewModel = koinViewModel())
    }

    composable<TopLevelScreen.Feed> {
        FeedScreen(viewModel = koinViewModel())
    }

    composable<TopLevelScreen.Bookmarks> {
        BookmarksScreen(viewModel = koinViewModel())
    }

    composable<TopLevelScreen.Infos> {
        InfoScreen(
            viewModel = koinViewModel(),
            onRequestLogin = onRequestLogin
        )
    }
}
