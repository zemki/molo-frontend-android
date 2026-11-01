package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.atom.OrganizationWithBadge
import news.molo.core.designsystem.component.atom.OverlayDeleteButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.LocalIsDarkTheme
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.calculatePercentageOfWindowHeight
import news.molo.core.util.helper.timeSinceNow
import news.molo.core.util.helper.toShortString
import news.molo.core.util.preview.ComponentPreview
import java.time.OffsetDateTime

sealed interface PublishedStyle {
    data class Date(val createdAt: OffsetDateTime) : PublishedStyle
    data object Preview : PublishedStyle
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityArticleCard(
    headline: String,
    content: String,
    imageURL: String?,
    organizationImageURL: String,
    organizationName: String,
    publishedStyle: PublishedStyle,
    viewCount: Int? = null,
    bookmarkCount: Int? = null,
    isDraft: Boolean = false,
    onClick: () -> Unit,
    onPublisherClicked: () -> Unit,
    onClickDelete: (() -> Unit)? = null,
) {
    val height = calculatePercentageOfWindowHeight(0.4f)
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        onClick = { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OrganizationWithBadge(
                    logoUrl = organizationImageURL,
                    categoryIconRes = R.drawable.icn_community,
                    onClick = onPublisherClicked
                )

                Spacer(modifier = Modifier.width(Spacing.Small))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Bottom),
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            onPublisherClicked()
                        },
                        text = organizationName,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(Spacing.Small))

                    when (publishedStyle) {
                        is PublishedStyle.Date ->
                            Text(
                                text = publishedStyle.createdAt.timeSinceNow(),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )

                        PublishedStyle.Preview -> Text(
                            text = "Preview",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.MoloOrange,
                            maxLines = 1
                        )
                    }

                }

                onClickDelete?.let { onCLickDelete ->
                    OverlayDeleteButton(
                        modifier = Modifier
                            .padding(start = Spacing.Small)
                            .align(Alignment.CenterVertically),
                        onClick = onCLickDelete
                    )
                }
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = headline,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString.fromHtml(content),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                ),
                minLines = 3,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (imageURL == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    ImageWithPlaceholder(
                        imageUri = imageURL,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(CornerRadius.Medium))
                    )

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
                                tint = when(isDarkTheme) {
                                    true -> Color.Black
                                    false -> Color.MoloOrange
                                },
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

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

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        CommunityArticleCard(
            headline = "Headline",
            content = "Thats how the article content could look like",
            imageURL = null,
            organizationImageURL = "",
            organizationName = "Organization name",
            publishedStyle = PublishedStyle.Date(OffsetDateTime.now()),
            onClick = { },
            viewCount = 1,
            bookmarkCount = 1,
            onPublisherClicked = { },
            onClickDelete = { },
            isDraft = true
        )
    }
}