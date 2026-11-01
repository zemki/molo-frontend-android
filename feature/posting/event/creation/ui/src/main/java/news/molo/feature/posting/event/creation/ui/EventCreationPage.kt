package news.molo.feature.posting.event.creation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.molecule.ContentCreationTextField
import news.molo.core.designsystem.component.molecule.HeadlineTextField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun EventCreationPage(
    headline: String,
    content: String,
    profilePictureUrl: String?,
    onHeadlineChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {

    PageTemplate {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageWithPlaceholder(
                modifier = Modifier
                    .size(52.dp)
                    .clip(MaterialTheme.shapes.medium),
                imageUri = profilePictureUrl
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            HeadlineTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                headline = headline,
                placeholder = stringResource(R.string.event_posting_title_hint),
                onHeadlineChange = onHeadlineChange
            )
        }

        ContentCreationTextField(
            content = content,
            placeholder = stringResource(R.string.event_posting_content_hint),
            onContentChange = onContentChange
        )
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        EventCreationPage(
            headline = "headline",
            content = "abc",
            onContentChange = { },
            onHeadlineChange = {},
            profilePictureUrl = null,
        )
    }
}