package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.atom.OrganizationWithBadge
import news.molo.core.designsystem.component.atom.OverlayDeleteButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.calculatePercentageOfWindowHeight
import news.molo.core.util.helper.timeSinceNow
import news.molo.core.util.helper.toShortString
import news.molo.core.util.preview.ComponentPreview
import java.time.OffsetDateTime

@Composable
fun NewsArticleCard(
    title: String,
    content: String,
    imageURL: String,
    organizationImageURL: String,
    organizationName: String,
    createdAt: OffsetDateTime,
    viewCount: Int? = null,
    bookmarkCount: Int? = null,
    onClick: () -> Unit,
    onOrganizationClicked: () -> Unit,
    onClickDelete: (() -> Unit)? = null,
) {
    ArticleCard(
        title = title,
        content = content,
        imageURL = imageURL,
        organizationName = organizationName,
        organizationWithBadge = {
            OrganizationWithBadge(
                logoUrl = organizationImageURL,
                categoryIconRes = R.drawable.icn_news,
                onClick = onOrganizationClicked
            )
        },
        createdAt = createdAt,
        viewCount = viewCount,
        bookmarkCount = bookmarkCount,
        onClick = onClick,
        onOrganizationClicked = onOrganizationClicked,
        onClickDelete = onClickDelete
    )
}

@Composable
fun OfficialArticleCard(
    title: String,
    content: String,
    imageURL: String,
    organizationImageURL: String,
    organizationName: String,
    createdAt: OffsetDateTime,
    viewCount: Int? = null,
    bookmarkCount: Int? = null,
    onClick: () -> Unit,
    onOrganizationClicked: () -> Unit,
    onClickDelete: (() -> Unit)? = null,
) {
    ArticleCard(
        title = title,
        content = content,
        imageURL = imageURL,
        organizationName = organizationName,
        organizationWithBadge = {
            OrganizationWithBadge(
                logoUrl = organizationImageURL,
                categoryIconRes = R.drawable.icn_offical,
                onClick = onOrganizationClicked
            )
        },
        createdAt = createdAt,
        viewCount = viewCount,
        bookmarkCount = bookmarkCount,
        onClick = onClick,
        onOrganizationClicked = onOrganizationClicked,
        onClickDelete = onClickDelete
    )
}

@Composable
internal fun ArticleCard(
    title: String,
    content: String,
    imageURL: String,
    organizationName: String,
    organizationWithBadge: @Composable RowScope.(onClick: () -> Unit) -> Unit,
    createdAt: OffsetDateTime,
    viewCount: Int? = null,
    bookmarkCount: Int? = null,
    onClick: () -> Unit,
    onOrganizationClicked: () -> Unit,
    onClickDelete: (() -> Unit)? = null,
) {
    val height = calculatePercentageOfWindowHeight(0.4f)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        onClick = { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
            ) {
                ImageWithPlaceholder(
                    contentScale = ContentScale.Crop,
                    imageUri = imageURL,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = CornerRadius.Medium,
                                topEnd = CornerRadius.Medium
                            )
                        )
                )

                onClickDelete?.let { onClickDelete ->
                    OverlayDeleteButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(Spacing.Small),
                        onClick = onClickDelete
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = Spacing.Medium,
                            end = Spacing.Medium,
                            bottom = Spacing.Medium
                        )
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(Spacing.Small))

                    Text(
                        modifier = Modifier.weight(1f),
                        text = content,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )

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

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = (-45).dp)
                        .padding(horizontal = Spacing.Small),
                    verticalAlignment = Alignment.Bottom
                ) {

                    organizationWithBadge.invoke(this, onOrganizationClicked)
                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = onOrganizationClicked
                            ),
                        text = organizationName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(Spacing.Small))

                    Text(
                        text = createdAt.timeSinceNow(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
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
        ArticleCard(
            title = "Article Title",
            content = "Thats how the article content could look like. winrugehreuhnehnuearheruhnreahnreiuanehareunhaenhuriahuearhinuaerhiuneaihnueanhrinuaaernhueaiuhnureaheriunhreianhreiaheuahaiunerhraeiunheranhreaiuhwinrugehreuhnehnuearheruhnreahnreiuanehareunhaenhuriahuearhinuaerhiuneaihnueanhrinuaaernhueaiuhnureaheriunhreianhreiaheuahaiunerhraeiunheranhreaiuhwinrugehreuhnehnuearheruhnreahnreiuanehareunhaenhuriahuearhinuaerhiuneaihnueanhrinuaaernhueaiuhnureaheriunhreianhreiaheuahaiunerhraeiunheranhreaiuhwinrugehreuhnehnuearheruhnreahnreiuanehareunhaenhuriahuearhinuaerhiuneaihnueanhrinuaaernhueaiuhnureaheriunhreianhreiaheuahaiunerhraeiunheranhreaiuh",
            imageURL = "",
            organizationWithBadge = { clickCallback ->
                OrganizationWithBadge(
                    logoUrl = "",
                    categoryIconRes = R.drawable.icn_news,
                    onClick = clickCallback
                )
            },
            organizationName = "Organization name",
            createdAt = OffsetDateTime.now(),
            onClick = { },
            onOrganizationClicked = {},
            onClickDelete = {}
        )
    }
}
