package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    placeholder: String = "Suchen",
    onQueryChanged: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = {
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChanged("") }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear query"
                )
            }
        }
    },
) {
    val focusManager = LocalFocusManager.current

    val placeHolderColor = when (MaterialTheme.isDarkTheme) {
        true -> Color(0xFF8E8E93)
        false -> Color(0xFF8E8E93)
    }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        windowInsets = WindowInsets(0, 0, 0, 0),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChanged,
                onSearch = {
                    focusManager.clearFocus()
                },
                expanded = false,
                colors = SearchBarDefaults.inputFieldColors(
                    unfocusedPlaceholderColor = placeHolderColor,
                    disabledPlaceholderColor = placeHolderColor,
                    focusedPlaceholderColor = placeHolderColor,
                    focusedTrailingIconColor = placeHolderColor,
                    disabledTrailingIconColor = placeHolderColor,
                    unfocusedTrailingIconColor = placeHolderColor,
                    disabledLeadingIconColor = placeHolderColor,
                    focusedLeadingIconColor = placeHolderColor,
                    unfocusedLeadingIconColor = placeHolderColor,
                    cursorColor = Color.MoloOrange
                ),
                onExpandedChange = { },
                placeholder = { Text(placeholder) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon"
                    )
                },
                trailingIcon = trailingIcon
            )
        },
        expanded = false,
        onExpandedChange = {},
        content = {}
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        SearchField(
            query = "",
            onQueryChanged = {}
        )
    }
}