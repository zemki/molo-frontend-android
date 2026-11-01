package news.molo.core.designsystem.component.molecule

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun LocationSearchField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onRequestLocationClick: () -> Unit,
) {
    SearchField(
        modifier = Modifier
            .then(modifier),
        query = query,
        onQueryChanged = onQueryChange,
        trailingIcon = {
            when (query.isEmpty()) {
                true -> IconButton(onClick = onRequestLocationClick) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "Clear query"
                    )
                }

                false -> IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear query"
                    )
                }
            }
        }
    )
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        LocationSearchField(
            query = "",
            onQueryChange = {},
            onRequestLocationClick = {}
        )
    }
}