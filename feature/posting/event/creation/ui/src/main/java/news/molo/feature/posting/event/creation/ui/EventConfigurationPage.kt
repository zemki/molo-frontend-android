package news.molo.feature.posting.event.creation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import news.molo.core.designsystem.component.atom.ClickableTextField
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.organism.ChipFlowRow
import news.molo.core.designsystem.component.organism.EventTimeRangeDialog
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.formatDateTimeRange
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Tag
import news.molo.domain.model.TagCategory
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun EventConfigurationPage(
    startDate: OffsetDateTime?,
    endDate: OffsetDateTime?,
    location: String,
    link: String,
    ticketLink: String,
    eventLocation: String,
    tagCategories: List<TagCategory>,
    onChangeLocationClick: () -> Unit,
    onDateRangeChange: (from: OffsetDateTime, until: OffsetDateTime) -> Unit,
    onLinkChange: (String) -> Unit,
    onTicketLinkChange: (String) -> Unit,
    onEventLocationChange: (String) -> Unit,
    onSelectTag: (Tag) -> Unit,
    onSelectCategory: (TagCategory) -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }
    val dateRange: String? = remember(startDate, endDate) {
        val start = startDate ?: return@remember null
        val end = endDate ?: return@remember null

        formatDateTimeRange(start = start, end = end)
    }

    if (showDatePicker) {
        EventTimeRangeDialog(
            onSave = { from, until ->
                onDateRangeChange(from, until)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
            initialStartDate = startDate,
            initialEndDate = endDate,
        )
    }


    PageTemplateWithPaddingSlot(
        modifier = Modifier.imePadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) { contentPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = contentPadding
        ) {

            item("inputs") {
                EventInputGroup(
                    eventLocation = eventLocation,
                    location = location,
                    dateRange = dateRange,
                    link = link,
                    ticketLink = ticketLink,
                    onDateSelectionClick = { showDatePicker = true },
                    onChangeLocationClick = onChangeLocationClick,
                    onEventLocationChange = onEventLocationChange,
                    onLinkChange = onLinkChange,
                    onTicketLinkChange = onTicketLinkChange
                )
            }

            item(key = "tag_header") {
                EventTagRowHeader()
            }

            items(tagCategories, key = { it.id }) { category ->
                TagFlowRow(
                    category = category,
                    onSelectTag = onSelectTag,
                    onSelectCategory = onSelectCategory
                )
            }
        }
    }
}

@Composable
private fun EventInputGroup(
    eventLocation: String,
    location: String,
    dateRange: String?,
    link: String,
    ticketLink: String,
    onChangeLocationClick: () -> Unit,
    onEventLocationChange: (String) -> Unit,
    onLinkChange: (String) -> Unit,
    onTicketLinkChange: (String) -> Unit,
    onDateSelectionClick: () -> Unit
) {
    TextField(
        value = eventLocation,
        placeholder = stringResource(R.string.event_posting_location_hint),
        onValueChange = onEventLocationChange
    )

    ClickableTextField(
        value = location,
        placeholder = stringResource(R.string.event_posting_address_hint),
        onClick = onChangeLocationClick,
    )

    ClickableTextField(
        value = dateRange.orEmpty(),
        placeholder = stringResource(R.string.event_posting_date_hint),
        onClick = onDateSelectionClick,
    )

    TextField(
        value = link,
        placeholder = stringResource(R.string.event_posting_link_hint),
        onValueChange = onLinkChange
    )

    TextField(
        value = ticketLink,
        placeholder = stringResource(R.string.event_posting_ticket_link_hint),
        onValueChange = onTicketLinkChange
    )
}

@Composable
private fun EventTagRowHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.Small)
        ) {
            Icon(Icons.Default.Tag, contentDescription = null, tint = Color.MoloOrange)
            Text(text = stringResource(R.string.event_posting_tags_headline), fontWeight = FontWeight.Medium, fontSize = 15.sp)
        }

        Text(text = stringResource(R.string.event_posting_tags_description))
    }
}

@Composable
private fun TagFlowRow(
    category: TagCategory,
    onSelectTag: (Tag) -> Unit,
    onSelectCategory: (TagCategory) -> Unit
) {
    ChipFlowRow(
        title = category.name,
        items = category.tags,
        chip = { tag ->
            FilterItem(
                title = tag.name,
                selected = tag.isSelected,
                onSelectedChange = { onSelectTag(tag) }
            )
        },
        onToggleAllSelected = { onSelectCategory(category) }
    )
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        EventConfigurationPage(
            location = "eew",
            startDate = null,
            endDate = null,
            onChangeLocationClick = {},
            onDateRangeChange = { _, _ -> },
            link = "-",
            ticketLink = "-",
            eventLocation = "-",
            onLinkChange = { },
            onTicketLinkChange = { },
            onEventLocationChange = { },
            tagCategories = emptyList(),
            onSelectTag = {},
            onSelectCategory = {},
        )
    }
}