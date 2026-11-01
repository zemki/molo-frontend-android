package news.molo.core.designsystem.component.organism

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ProgressIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ListTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isRefreshing: Boolean,
    showLoading: Boolean,
    isEmpty: Boolean = false,
    emptyContent: (@Composable () -> Unit)? = null,
    onRefresh: () -> Unit,
    header: (@Composable LazyItemScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        ListTemplate(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            header = header,
            content = {
                when {
                    showLoading -> item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.Medium),
                            contentAlignment = Alignment.Center
                        ) {
                            ProgressIndicator()
                        }
                    }

                    isEmpty && emptyContent != null -> item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            emptyContent()
                        }
                    }

                    else -> content()
                }
            }
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        RefreshableList(
            isRefreshing = false,
            onRefresh = {},
            showLoading = true,
        ) {
            items(listOf("abc", "abcd", "abcde"), key = { it }) {
                Text(text = it)
            }
        }
    }
}