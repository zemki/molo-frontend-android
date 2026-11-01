package news.molo.feature.event.detail.ui.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import news.molo.core.designsystem.component.molecule.DetailsTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
internal fun DetailedEventTopAppBar(
    isBookmarked: Boolean,
    onNavigateBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onBookmarkedClicked: () -> Unit,
    onReportClicked: () -> Unit,
    onAddToCalendarClicked: () -> Unit
) {
    var moreMenuExpanded by remember { mutableStateOf(false) }

    DetailsTopAppBar(
        isBookmarked = isBookmarked,
        onClickBack = onNavigateBackClicked,
        onClickBookmark = onBookmarkedClicked,
        onClickShare = onShareClicked,
        moreMenuExpanded = moreMenuExpanded,
        moreMenuContent = {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.event_add_to_calendar_action_text)) },
                onClick = {
                    moreMenuExpanded = false
                    onAddToCalendarClicked()
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.event_report_action_text)) },
                onClick = {
                    moreMenuExpanded = false
                    onReportClicked()
                }
            )
        },
        onMoreMenuExpandedChange = {
            moreMenuExpanded = it
        }
    )
}

private class Provider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: Boolean
) {
    ComponentPreviewTemplate {
        DetailedEventTopAppBar(
            isBookmarked = state,
            onNavigateBackClicked = { },
            onShareClicked = { },
            onBookmarkedClicked = { },
            onReportClicked = { },
            onAddToCalendarClicked = { }
        )
    }
}