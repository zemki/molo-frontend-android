package news.molo.feature.event.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.molecule.FilterRow
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.helper.formatDate
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.event.presentation.EventFilter
import news.molo.feature.event.presentation.EventFilter.Calendar


@Composable
internal fun EventFilterRow(
    filter: List<EventFilter>,
    selectedFilter: EventFilter?,
    onSelectedChange: (EventFilter) -> Unit
) {
    FilterRow(
        modifier = Modifier.padding(horizontal = Spacing.Medium),
        items = filter,
        filterItem = { filter ->
            FilterItem(
                title = filter.getFilterTitle(),
                leadingIcon = {
                    Icon(
                        painterResource(filter.iconRes),
                        contentDescription = null,
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                selected = filter == selectedFilter,
                onSelectedChange = {
                    onSelectedChange(filter)
                }
            )
        }
    )
}

@Composable
private fun EventFilter.getFilterTitle(): String = when (this) {
    is Calendar -> {
        val from = from
        val to = to
        if (from != null && to != null) {
            "${from.formatDate()} - ${to.formatDate()}"
        } else {
            stringResource(titleRes)
        }
    }

    else -> stringResource(titleRes)
}


@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        EventFilterRow(
            filter = EventFilter.all(),
            selectedFilter = EventFilter.all().first(),
            onSelectedChange = {}
        )
    }
}