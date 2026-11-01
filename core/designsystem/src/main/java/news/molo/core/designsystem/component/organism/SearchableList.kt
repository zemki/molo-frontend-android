package news.molo.core.designsystem.component.organism

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.molecule.SearchField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ListTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun SearchableList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    query: String,
    onQueryChanged: (String) -> Unit,
    content: LazyListScope.() -> Unit
) {
    ListTemplate(
        modifier = modifier,
        contentPadding = contentPadding,
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
        SearchableList(
            query = "ABC",
            onQueryChanged = {}
        ) {
            items(listOf("abc", "abcd", "abcde"), key = { it }) {
                Text(text = it)
            }
        }
    }
}