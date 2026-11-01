package news.molo.android.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import de.convelop.forge.event.EventBus
import de.convelop.forge.navigation.NavigationEvent
import news.molo.android.navigation.BottomNavItem
import news.molo.android.presentation.AppState
import news.molo.core.designsystem.component.molecule.BottomNavigationBar
import news.molo.core.designsystem.component.molecule.MoloTopAppBar
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.navigation.Screen
import news.molo.core.navigation.TopLevelScreen
import news.molo.core.resource.R
import news.molo.domain.model.ConfigurationType
import news.molo.domain.model.TabSelectionType
import news.molo.feature.explore.ui.GlobalSearchBar
import org.koin.compose.koinInject

private val APP_TABS = listOf(
    BottomNavItem(R.string.bottom_navigation_feed, TopLevelScreen.Feed, R.drawable.icn_feed),
    BottomNavItem(R.string.bottom_navigation_events, TopLevelScreen.Events, R.drawable.icn_events),
    BottomNavItem(
        R.string.bottom_navigation_bookmarks,
        TopLevelScreen.Bookmarks,
        R.drawable.icn_bookmarks
    ),
    BottomNavItem(R.string.bottom_navigation_infos, TopLevelScreen.Infos, R.drawable.icn_dots_more)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainContent(
    bearer: String,
    rootNavController: NavHostController,
    bottomNavController: NavHostController,
    appState: AppState,
    executeIntent: (AppState.Intent) -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val eventBus = koinInject<EventBus<UIEvent>>()
    val snackBarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val selectedTab by remember(navBackStackEntry) {
        derivedStateOf { findSelectedTab(APP_TABS, navBackStackEntry?.destination) }
    }

    HandleOneTimeToken(
        bearer = bearer,
        executeIntent = executeIntent,
        snackBarHostState = snackBarHostState
    )

    UIEventHandler(uiEvent = eventBus.events, snackBarHostState = snackBarHostState)

    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    val showSearchBar by remember(selectedTab) {
        derivedStateOf {
            selectedTab.route == TopLevelScreen.Events || selectedTab.route == TopLevelScreen.Feed
        }
    }
    val topAppBarHeight = 64.dp
    val searchBarHeight = 72.dp
    val searchBarTopPadding by animateDpAsState(
        targetValue = if (isSearchActive || !showSearchBar) 0.dp else topAppBarHeight,
        label = "SearchBarAnimation"
    )

    Box(Modifier.fillMaxSize()) {
        ScreenScaffold(
            topBar = {
                Column {
                    AppTopBar(appState, rootNavController, selectedTab, executeIntent)
                    if (showSearchBar) Spacer(modifier = Modifier.height(searchBarHeight))
                }
            },
            bottomBar = {
                if (!isSearchActive) {
                    AppBottomBar(APP_TABS, selectedTab) {
                        navigateTab(bottomNavController, it)
                    }
                }
            },
            floatingActionButton = {
                if (!isSearchActive) {
                    FabContent(appState.isLoggedIn, executeIntent)
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        ) {
            AppContent(
                navController = bottomNavController,
                executeIntent = executeIntent,
                bottomSheetState = appState.bottomSheetState,
                onNavigationEvent = onNavigationEvent,
            )
        }

        if (showSearchBar) {
            GlobalSearchBar(
                query = appState.query,
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                onQueryChange = { executeIntent(AppState.Intent.SetQuery(it)) },
                onSearch = { query, type ->
                    isSearchActive = false
                    executeIntent(
                        AppState.Intent.NavigateToSearchResults(
                            query = query,
                            type = type,
                            selectedTab = when (selectedTab.route) {
                                is TopLevelScreen.Events -> TabSelectionType.Events
                                else -> TabSelectionType.Article
                            }
                        )
                    )
                },
                searchResults = appState.searchHistory,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = searchBarTopPadding),
                onClickDeleteItem = { executeIntent(AppState.Intent.DeleteSearchHistoryEntry(it.id)) },
                onClickClearHistory = { executeIntent(AppState.Intent.DeleteSearchHistory) }
            )
        }
    }
}

@Composable
private fun FabContent(isLoggedIn: Boolean, executeIntent: (AppState.Intent) -> Unit) {
    if (isLoggedIn) {
        PostingFab(
            onClickToArticle = { executeIntent(AppState.Intent.ToArticlePostCreation) },
            onClickToEvent = { executeIntent(AppState.Intent.ToEventPostCreation) }
        )
    } else {
        LoginFab(
            onClick = { executeIntent(AppState.Intent.ShowLogin) }
        )
    }
}

private fun navigateTab(navController: NavHostController, tab: BottomNavItem) {
    navController.navigate(tab.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
        launchSingleTop = true
    }
}

@Composable
private fun HandleOneTimeToken(
    bearer: String,
    executeIntent: (AppState.Intent) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    /*
        This is a workaround.
        We use the argument of the NavBackStack which resubmits the value once we navigate back to it and we only want to set the token once
     */
    val currentExecuteIntent by rememberUpdatedState(executeIntent)

    var tokenGotUsed by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(bearer) {
        if (bearer.isNotBlank() && !tokenGotUsed) {
            currentExecuteIntent(AppState.Intent.SetBearer(bearer))
            tokenGotUsed = true
            snackBarHostState.showSnackbar(message = "Erfolgreich angemeldet")
        }
    }
}

@Composable
private fun AppBottomBar(
    tabs: List<BottomNavItem>,
    selectedTab: BottomNavItem,
    onTabClicked: (BottomNavItem) -> Unit
) {
    BottomNavigationBar(
        items = tabs,
        selectedItem = selectedTab,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(it.icon),
                contentDescription = stringResource(it.title)
            )
        },
        label = {
            Text(
                text = stringResource(it.title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        onItemSelected = onTabClicked
    )
}

@Composable
private fun AppTopBar(
    appState: AppState,
    rootNavController: NavHostController,
    selectedTab: BottomNavItem,
    executeIntent: (AppState.Intent) -> Unit
) {
    MoloTopAppBar(
        location = appState.selectedLocation,
        isLoggedIn = appState.isLoggedIn,
        avatarUrl = appState.selfUserData?.avatarUrl,
        username = appState.selfUserData?.username.orEmpty(),
        onClickTags = {
            rootNavController.navigate(
                Screen.TagSelection(
                    getTagTypeForSelectedTab(
                        selectedTab
                    )
                )
            )
        },
        onClickLocation = {
            rootNavController.navigate(
                Screen.LocationSelection(
                    ConfigurationType.Default,
                    null
                )
            )
        },
        onClickProfile = {
            when (appState.isLoggedIn) {
                true -> appState.selfUserData?.organizationId?.let {
                    rootNavController.navigate(
                        Screen.OrganizationDetails(
                            it,
                            TabSelectionType.Article
                        )
                    )
                }

                false -> executeIntent(AppState.Intent.ShowLogin)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PostingFab(
    onClickToArticle: () -> Unit,
    onClickToEvent: () -> Unit
) {

    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    BackHandler(fabMenuExpanded) { fabMenuExpanded = !fabMenuExpanded }

    FloatingActionButtonMenu(
        expanded = fabMenuExpanded,
        button = {
            ToggleFloatingActionButton(
                modifier = Modifier
                    .semantics {
                        traversalIndex = -1f
                        stateDescription =
                            if (fabMenuExpanded) "Expanded" else "Collapsed"
                        contentDescription = "Toggle menu"
                    }
                    .animateFloatingActionButton(
                        visible = true,
                        alignment = Alignment.BottomEnd,
                    ),
                containerColor = {
                    Color.MoloOrange
                },
                checked = fabMenuExpanded,
                onCheckedChange = { fabMenuExpanded = !fabMenuExpanded },
            ) {
                val imageVector by remember {
                    derivedStateOf {
                        if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                    }
                }
                Icon(
                    painter = rememberVectorPainter(imageVector),
                    contentDescription = null,
                    modifier = Modifier.animateIcon(
                        { checkedProgress },
                        color = { Color.White }),
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = {
                fabMenuExpanded = false
                onClickToArticle()
            },
            icon = {
                Icon(
                    painterResource(R.drawable.icn_feed),
                    contentDescription = null
                )
            },
            containerColor = Color.MoloOrange,
            contentColor = Color.White,
            text = { Text(text = stringResource(R.string.main_content_article_fab_title)) },
        )

        FloatingActionButtonMenuItem(
            onClick = {
                fabMenuExpanded = false
                onClickToEvent()
            },
            icon = {
                Icon(
                    painterResource(R.drawable.icn_events),
                    contentDescription = null
                )
            },
            containerColor = Color.MoloOrange,
            contentColor = Color.White,
            text = { Text(text = stringResource(R.string.main_content_event_fab_title)) },
        )
    }
}

@Composable
private fun LoginFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.MoloOrange,
        contentColor = Color.White
    ) {
        Icon(
            painter = painterResource(R.drawable.icn_new_post),
            contentDescription = null,
            tint = Color.White
        )
    }
}

private fun findSelectedTab(
    tabs: List<BottomNavItem>,
    currentDestination: NavDestination?
): BottomNavItem = tabs.find { tab ->
    currentDestination?.hierarchy?.any { it.route == tab.route::class.qualifiedName } == true
} ?: tabs.first()


private fun getTagTypeForSelectedTab(tab: BottomNavItem): TabSelectionType = when (tab.route) {
    is TopLevelScreen.Events -> TabSelectionType.Events
    else -> TabSelectionType.Article
}