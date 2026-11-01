package news.molo.core.designsystem.component.organism

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import news.molo.core.designsystem.component.molecule.PageIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun <T> PagerWithIndicator(
    modifier: Modifier = Modifier,
    tabs: List<T>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    tabText: (@Composable (T) -> Unit)? = null,
    tabIcon: (@Composable (T) -> Unit)? = null,
    contentForPage: @Composable (T) -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(selectedTab).coerceAtLeast(0),
        pageCount = { tabs.size }
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val currentTab = tabs[pagerState.currentPage]
        if (selectedTab != currentTab) {
            onTabSelected(currentTab)
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {

        Column(modifier = modifier.fillMaxSize()) {
            PageIndicator(
                selectedTabIndex = tabs.indexOf(selectedTab),
                tabCount = tabs.size,
                tabText = tabText?.let { textFn ->
                    { index -> textFn(tabs[index]) }
                },
                tabIcon = tabIcon?.let { iconFn ->
                    { index -> iconFn(tabs[index]) }
                },
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                beyondViewportPageCount = tabs.size,
                key = { index -> tabs[index].toString() },
            ) { page ->
                contentForPage(tabs[page])
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val tabs = listOf("Tab #1", "Tab #2")
    ComponentPreviewTemplate {
        PagerWithIndicator(
            tabs = tabs,
            selectedTab = tabs.first(),
            onTabSelected = {},
            tabText = { tab ->
                when (tab) {
                    "Tab #1" -> Text("Test")
                    "Tab #2" -> Text("Test anc")
                }
            },
            contentForPage = { tab ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "The selected page content for $tab")
                }
            }
        )
    }
}