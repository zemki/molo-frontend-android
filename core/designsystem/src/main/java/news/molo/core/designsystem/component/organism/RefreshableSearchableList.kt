package news.molo.core.designsystem.component.organism

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.molecule.SearchField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableSearchableList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    query: String,
    isRefreshing: Boolean,
    showLoading: Boolean,
    onRefresh: () -> Unit,
    onQueryChanged: (String) -> Unit,
    content: LazyListScope.() -> Unit
) {

    RefreshableList(
        modifier = modifier,
        contentPadding = contentPadding,
        isRefreshing = isRefreshing,
        showLoading = showLoading,
        onRefresh = onRefresh,
        header = {
            SearchField(
                query = query,
                onQueryChanged = onQueryChanged
            )
        },
        content = content
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        RefreshableSearchableList(
            query = "ABC",
            isRefreshing = false,
            onRefresh = {},
            showLoading = true,
            onQueryChanged = {}
        ) {
            items(listOf("abc", "abcd", "abcde"), key = { it }) {
                Text(text = it)
            }
        }
    }
}