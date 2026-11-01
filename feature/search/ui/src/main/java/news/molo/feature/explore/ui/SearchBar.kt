package news.molo.feature.explore.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.SearchHistoryEntry
import news.molo.domain.model.SearchHistoryType
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchBar(
    query: String,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onSearch: (query: String, type: SearchHistoryType) -> Unit,
    searchResults: List<SearchHistoryEntry>,
    onClickDeleteItem: (SearchHistoryEntry) -> Unit,
    onClickClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioSearchPlaceholderText = stringResource(R.string.search_bar_audio_placeholder)
    val speechNotSupportedText = stringResource(R.string.search_bar_speech_input_not_supported)
    val horizontalPadding by animateDpAsState(if (active) 0.dp else Spacing.Medium)
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                onSearch(results[0], SearchHistoryType.Audio)
            }
        }
    }
    SearchBar(
        modifier = modifier
            .semantics { traversalIndex = 0f }
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    if (query.isBlank()) {
                        onActiveChange(false)
                        return@InputField
                    }
                    onSearch(query, SearchHistoryType.Text)
                    onQueryChange("")
                    onActiveChange(false)
                },
                expanded = active,
                onExpandedChange = onActiveChange,
                trailingIcon = {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            putExtra(
                                RecognizerIntent.EXTRA_PROMPT,
                                audioSearchPlaceholderText
                            )
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                        }
                        try {
                            speechLauncher.launch(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, speechNotSupportedText, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice Search")
                    }
                },
                placeholder = { Text(stringResource(R.string.search_bar_placeholder)) }
            )
        },
        expanded = active,
        onExpandedChange = onActiveChange,
    ) {

        Column(Modifier.verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.Medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.search_last_searches_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = onClickClearHistory) {
                    Text(text = stringResource(R.string.search_clear_history_text))
                }

            }
            searchResults.forEach { result ->
                SearchHistoryItem(
                    item = result,
                    onClick = {
                        onSearch(result.query, result.type)
                        onActiveChange(false)
                    },
                    onClickDelete = { onClickDeleteItem(result) }
                )
            }
        }
    }

}

@Composable
private fun SearchHistoryItem(
    item: SearchHistoryEntry,
    onClick: () -> Unit,
    onClickDelete: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            if (item.type == SearchHistoryType.Audio) {
                Icon(
                    modifier = Modifier.padding(end = Spacing.Small),
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = item.query,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onClickDelete) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        HorizontalDivider()
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        GlobalSearchBar(
            query = "abc",
            onQueryChange = { },
            onSearch = {_ ,_ -> },
            searchResults = listOf(
                SearchHistoryEntry(
                    query = "weguhewbeguhbbguhwebhughbuewgbhuwegbhuwbheugwebhugbhweugbhuwhbuwgehbuweg",
                    type = SearchHistoryType.Audio
                )
            ),
            active = true,
            onActiveChange = { },
            onClickDeleteItem = {},
            onClickClearHistory = { }
        )
    }
}