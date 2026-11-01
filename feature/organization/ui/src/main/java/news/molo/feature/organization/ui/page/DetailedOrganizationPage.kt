package news.molo.feature.organization.ui.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import news.molo.core.designsystem.component.molecule.OrganizationHeader
import news.molo.core.designsystem.component.molecule.PageIndicator
import news.molo.core.designsystem.component.organism.CollapsingHeaderPage
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.TabSelectionType

@Composable
internal fun DetailedOrganizationPage(
    tabs: List<TabSelectionType>,
    selectedTab: TabSelectionType,
    organizationLogoUrl: String,
    organizationName: String,
    organizationDescription: String,
    titleForType: (TabSelectionType) -> Int,
    tabContent: @Composable (TabSelectionType, LazyListState) -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(selectedTab),
        pageCount = { tabs.size }
    )

    val coroutineScope = rememberCoroutineScope()
    val articleListState = rememberLazyListState()
    val eventListState = rememberLazyListState()

    val collapseProgress by remember(pagerState.currentPage) {
        derivedStateOf {
            val listState = when (tabs[pagerState.currentPage]) {
                TabSelectionType.Article -> articleListState
                TabSelectionType.Events -> eventListState
            }

            val scrollOffset = listState.firstVisibleItemScrollOffset.toFloat()
            val itemIndex = listState.firstVisibleItemIndex

            if (itemIndex > 0) {
                1f
            } else {
                minOf(scrollOffset / 300f, 1f)
            }
        }
    }

    CollapsingHeaderPage(
        header = {
            OrganizationHeader(
                modifier = Modifier.padding(Spacing.Medium),
                imageUrl = organizationLogoUrl,
                name = organizationName,
                description = organizationDescription
            )
        },
        collapseProgress = collapseProgress
    ) {
        PageIndicator(
            selectedTabIndex = pagerState.currentPage,
            tabCount = TabSelectionType.entries.count(),
            tabText = { index ->
                val type = TabSelectionType.entries[index]
                Text(stringResource(titleForType(type)))
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
            key = { index -> tabs[index].name },
        ) { page ->
            val tabType = tabs[page]

            val currentListState = when (tabType) {
                TabSelectionType.Article -> articleListState
                TabSelectionType.Events -> eventListState
            }

            tabContent(tabType, currentListState)
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DetailedOrganizationPage(
            tabs = TabSelectionType.entries,
            selectedTab = TabSelectionType.Article,
            organizationLogoUrl = "TODO()",
            organizationName = "Convelop",
            organizationDescription = "We make Apps",
            titleForType = { 0 },
            tabContent = { _, _ -> }
        )
    }
}