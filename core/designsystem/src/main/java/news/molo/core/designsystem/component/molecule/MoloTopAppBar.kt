package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MoloTopAppBar(
    modifier: Modifier = Modifier,
    location: String,
    isLoggedIn: Boolean = false,
    avatarUrl: String? = null,
    username: String = "",
    onClickLocation: () -> Unit,
    onClickTags: () -> Unit,
    onClickProfile: () -> Unit
) {

    val iconColor = when (MaterialTheme.isDarkTheme) {
        true -> Color(0xFFF58330)
        false -> Color.White
    }

    DefaultTopAppBar(
        modifier = Modifier.then(modifier),
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = iconColor,
            actionIconContentColor = iconColor
        ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickLocation() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = Spacing.Small),
                    painter = painterResource(R.drawable.icn_molo),
                    contentDescription = null,
                    tint = iconColor
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }, actions = {
            IconButton(onClick = onClickLocation) {
                Icon(painterResource(R.drawable.icn_location_pin), contentDescription = null)
            }
            IconButton(onClick = onClickTags) {
                Icon(painterResource(R.drawable.icn_hashtag), contentDescription = null)
            }

            IconButton(onClick = onClickProfile) {
                if (!isLoggedIn) {
                    Box(
                        modifier = Modifier
                            .size(
                                IconButtonDefaults.mediumContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Uniform
                                )
                            )
                            .border(CardDefaults.outlinedCardBorder(true), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person, contentDescription = null
                        )
                    }

                } else {
                    when {
                        avatarUrl.isNullOrBlank() -> Box(
                            modifier = Modifier
                                .size(
                                    IconButtonDefaults.mediumContainerSize(
                                        IconButtonDefaults.IconButtonWidthOption.Uniform
                                    )
                                )
                                .border(CardDefaults.outlinedCardBorder(true), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = username.first().toString())
                        }

                        else -> {
                            ImageWithPlaceholder(
                                imageUri = avatarUrl,
                                modifier = Modifier
                                    .size(
                                        IconButtonDefaults.mediumContainerSize(
                                            IconButtonDefaults.IconButtonWidthOption.Uniform
                                        )
                                    )
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    onClickBack: () -> Unit,
    onClickShare: () -> Unit,
    onClickBookmark: () -> Unit,
    moreMenuExpanded: Boolean = false,
    onMoreMenuExpandedChange: (Boolean) -> Unit = {},
    moreMenuContent: (@Composable ColumnScope.() -> Unit)? = null
) {

    TopAppBar(
        modifier = Modifier.then(modifier),
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    painterResource(R.drawable.icn_arrow_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = onClickShare
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }

            IconButton(onClick = onClickBookmark) {
                Icon(
                    imageVector = when (isBookmarked) {
                        true -> Icons.Default.Bookmark
                        false -> Icons.Default.BookmarkBorder
                    },
                    contentDescription = null
                )
            }

            IconButton(
                onClick = { onMoreMenuExpandedChange(true) }
            ) {
                Icon(
                    painterResource(R.drawable.icn_more_dots),
                    contentDescription = null
                )
            }

            moreMenuContent?.let { content ->
                DropdownMenu(
                    expanded = moreMenuExpanded,
                    onDismissRequest = { onMoreMenuExpandedChange(false) }
                ) {
                    content()
                }
            }
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        MoloTopAppBar(
            location = "Bremen + 10km ufewuw8efuweffuewjwfeujwefewefewfwfeweffewwefwfewefwefwef",
            onClickTags = {},
            onClickLocation = {},
            onClickProfile = {},
            username = "Janem",
            isLoggedIn = true
        )
    }
}