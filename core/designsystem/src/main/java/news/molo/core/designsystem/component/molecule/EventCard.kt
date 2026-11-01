package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.atom.OverlayDeleteButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.LocalIsDarkTheme
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.calculatePercentageOfWindowHeight
import news.molo.core.util.helper.toShortString
import news.molo.core.util.preview.ComponentPreview
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(
    title: String,
    content: String,
    date: OffsetDateTime,
    location: String,
    city: String,
    organizationName: String,
    imageURL: String?,
    viewCount: Int? = null,
    bookmarkCount: Int? = null,
    isDraft: Boolean = false,
    onClick: () -> Unit,
    onClickPublisher: () -> Unit,
    onClickDelete: (() -> Unit)? = null,
) {
    val height = calculatePercentageOfWindowHeight(0.45f)
    val weekdayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.GERMAN)
    val restPattern = if (date.hour == 0 && date.minute == 0) {
        "d. MMMM"
    } else {
        "d. MMMM, HH:mm 'Uhr'"
    }
    val restFormatter = DateTimeFormatter.ofPattern(restPattern, Locale.GERMAN)

    val dateText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(weekdayFormatter.format(date))
        }
        append(", ${restFormatter.format(date)}")
    }

    val locationText = buildAnnotatedString {
        if (location.isNotBlank()) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(location)
            }
            if (city.isNotBlank()) append(", $city")
        } else if (city.isNotBlank()) {
            append(city)
        }
    }

    val isDarkTheme = LocalIsDarkTheme.current
    val colorSchema = MaterialTheme.colorScheme

    val containerColor = remember(isDarkTheme, isDraft, colorSchema) {
        when (isDarkTheme) {
            true if (isDraft) -> Color(0xFF848484)
            false if (isDraft) -> Color(0xFFFFEFE5)
            else -> colorSchema.surfaceContainerLow
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        onClick = { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {

            Box {

                if (imageURL == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.45f)
                            .clip(RoundedCornerShape(CornerRadius.Medium))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icn_add_image),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                } else {
                    ImageWithPlaceholder(
                        imageUri = imageURL,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.45f)
                            .clip(RoundedCornerShape(CornerRadius.Medium))
                            .background(Color.LightGray)
                    )
                }



                onClickDelete?.let { onClickDelete ->
                    OverlayDeleteButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(Spacing.Small),
                        onClick = onClickDelete
                    )
                }

                if (isDraft) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White, shape = CircleShape)
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_draft),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(modifier = Modifier.height(Spacing.Small))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(13.dp),
                        painter = painterResource(R.drawable.icn_events),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(Spacing.Small))
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.Small))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(13.dp),
                        painter = painterResource(R.drawable.icn_pin_filled),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(Spacing.Small))
                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.Small))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Spacing.Small))

                Text(
                    modifier = Modifier.weight(1f),
                    text = AnnotatedString.fromHtml(content),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(Spacing.Small))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onClickPublisher()
                        },
                        text = "@$organizationName",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    viewCount?.let {
                        IconWithText(
                            icon = Icons.Outlined.Visibility,
                            text = viewCount.toShortString()
                        )
                    }

                    Spacer(modifier = Modifier.width(Spacing.Medium))

                    bookmarkCount?.let {
                        IconWithText(
                            icon = Icons.Outlined.BookmarkBorder,
                            text = bookmarkCount.toString()
                        )
                    }
                }

            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        EventCard(
            title = "Event title",
            content = "Thats how the content of a event could look like fwehf78wh0fw78efh7wefh7weh70fweh78fweh78fh7wef7hwef0h78w7eh8fweh78fwe7f7wef7hwef7hwe87efhwe78hefwe78hf8w7hf708whf78hwef78h7ewhfwe78hfweh78f7h8w",
            date = OffsetDateTime.now(),
            location = "Am Fallturm",
            city = "Bremen",
            imageURL = "",
            organizationName = "Bremerkunst",
            onClick = { },
            viewCount = 1,
            bookmarkCount = 1,
            onClickPublisher = {},
            onClickDelete = {},
            isDraft = true
        )
    }
}