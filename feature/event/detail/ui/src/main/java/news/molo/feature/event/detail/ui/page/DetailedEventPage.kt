package news.molo.feature.event.detail.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.withLink
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.molecule.Map
import news.molo.core.designsystem.component.molecule.OrganizerCard
import news.molo.core.designsystem.component.molecule.TicketButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.calculatePercentageOfWindowHeight
import news.molo.core.util.helper.openLocationInMaps
import news.molo.core.util.helper.rememberCustomTabsLauncher
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event
import news.molo.feature.event.detail.ui.component.EventInfoRow
import news.molo.feature.event.detail.ui.component.EventTagsSection

@Composable
internal fun DetailedEventPage(
    event: Event,
    onOrganizerClick: (Int) -> Unit,
) {

    val context = LocalContext.current
    val launchCustomTab = rememberCustomTabsLauncher()

    PageTemplateWithPaddingSlot(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) { paddingValues ->

        ImageWithPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(calculatePercentageOfWindowHeight(0.25f)),
            imageUri = event.imageURL
        )

        if (event.imageSourceName.isNotBlank()) {
            ImageSourceText(event.imageSourceName)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {

            if (event.hasTicket) {
                TicketButton(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    launchCustomTab(event.ticketURL)
                }
            }

            EventTextContent(
                content = event.content,
                title = event.title,
                link = event.link
            )

            EventInfoRow(event = event)

            if (event.hasLocation) {
                LocationMap(
                    latitude = event.latitude,
                    longitude = event.longitude,
                    onMapClick = {
                        context.openLocationInMaps(
                            event.latitude,
                            event.longitude
                        )
                    }
                )
            }

            OrganizerCard(
                imageUrl = event.organizationImageUrl,
                title = event.organizationName,
                location = event.eventLocation,
                onClick = { onOrganizerClick(event.organization) }
            )

            EventTagsSection(event.tags)
        }
    }
}

@Composable
private fun ColumnScope.ImageSourceText(source: String) {
    Text(
        text = "© $source",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(
                horizontal = Spacing.Medium,
                vertical = Spacing.ExtraSmall
            )
            .align(Alignment.End)
    )
}

@Composable
private fun EventTextContent(content: String, title: String, link: String) {

    val contentText = buildAnnotatedString {
        append(AnnotatedString.fromHtml(content).trimEnd())

        link.takeUnless { it.isBlank() }?.let { link ->
            appendLine()
            appendLine()
            withLink(
                LinkAnnotation.Url(
                    link,
                    TextLinkStyles(style = SpanStyle(color = Color.MoloOrange))
                )
            ) {
                append(text = stringResource(R.string.event_read_more_link))
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = Spacing.Small),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = contentText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LocationMap(latitude: Double, longitude: Double, onMapClick: () -> Unit) {
    Map(
        modifier = Modifier
            .height(calculatePercentageOfWindowHeight(0.3f)),
        lat = latitude,
        lng = longitude,
        onMapClick = onMapClick
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DetailedEventPage(
            event = Event.mock.copy(ticketURL = "abc"),
            onOrganizerClick = {}
        )
    }
}