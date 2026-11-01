package news.molo.feature.event.detail.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import news.molo.core.designsystem.component.molecule.InfoRow
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.InfoRowGroup
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Event
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun EventInfoRow(event: Event) {
    val contentColor = when (MaterialTheme.isDarkTheme) {
        true -> Color.White
        false -> MaterialTheme.colorScheme.primary
    }

    val weekdayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.GERMAN)
    val restPattern = if (event.eventDate.hour == 0 && event.eventDate.minute == 0) {
        "d. MMMM"
    } else {
        "d. MMMM, HH:mm 'Uhr'"
    }
    val restFormatter = DateTimeFormatter.ofPattern(restPattern, Locale.GERMAN)
    val dateText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(weekdayFormatter.format(event.eventDate))
        }
        append(", ${restFormatter.format(event.eventDate)}")
    }

    val locationText = buildAnnotatedString {
        if (event.eventLocation.isNotBlank()) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(event.eventLocation)
            }
            if (event.town.isNotBlank()) append(", ${event.town}")
        } else if (event.town.isNotBlank()) {
            append(event.town)
        }
    }

    Column {
        if (event.eventLocation.isNotBlank() && locationText.isNotBlank()) {
            Text(
                modifier = Modifier.padding(bottom = Spacing.Small),
                text = stringResource(R.string.event_date_location_info_row_title),
                style = MaterialTheme.typography.titleMedium
            )

            LocationInfos(
                date = dateText,
                location = locationText,
                iconColor = contentColor
            )
        }
    }
}

@Composable
private fun LocationInfos(
    date: AnnotatedString,
    location: AnnotatedString,
    iconColor: Color
) {
    InfoRowGroup {
        InfoRow(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.icn_time),
                    contentDescription = null,
                    tint = iconColor
                )
            },
            text = date
        )

        InfoRow(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.icn_pin_filled),
                    contentDescription = null,
                    tint = iconColor
                )
            },
            text = location
        )
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        EventInfoRow(Event.mock)
    }
}