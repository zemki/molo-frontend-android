package news.molo.core.designsystem.component.organism

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.ProgressIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.helper.calculatePercentageOfWindowWidth
import news.molo.core.util.preview.ComponentPreview

@Composable
fun HorizontalSnappingList(
    pagerState: PagerState,
    headline: @Composable ColumnScope.() -> Unit,
    placeholder: @Composable ColumnScope.() -> Unit,
    isLoading: Boolean,
    loadingContent: @Composable ColumnScope.() -> Unit,
    content: @Composable PagerScope.(index: Int) -> Unit
) {
    Column {
        headline()
        Spacer(modifier = Modifier.height(Spacing.Small))

        Crossfade(
            targetState = isLoading,
            label = "HorizontalSnappingListTransition"
        ) { isLoading ->
            when {
                isLoading -> loadingContent()

                pagerState.pageCount == 0 -> {
                    placeholder()
                }

                else -> HorizontalPager(
                    state = pagerState,
                    pageSpacing = Spacing.Small,
                    contentPadding = PaddingValues(
                        start = Spacing.Medium,
                        end = calculatePercentageOfWindowWidth(0.1f)
                    )
                ) { index ->
                    content(index)
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        val filter = listOf("Page #1", "Page #2")
        HorizontalSnappingList(
            pagerState = rememberPagerState { filter.size },
            headline = { Text(text = "Headline") },
            placeholder = {},
            isLoading = false,
            loadingContent = {
                ProgressIndicator()
            },
            content = {
                Text(text = filter[it])
            }
        )
    }
}