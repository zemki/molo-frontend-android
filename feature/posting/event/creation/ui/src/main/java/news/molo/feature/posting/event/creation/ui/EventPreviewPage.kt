package news.molo.feature.posting.event.creation.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.SecondaryButton
import news.molo.core.designsystem.component.molecule.EventCard
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.formatToHtml
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.picker.ImagePickerSheet
import java.time.OffsetDateTime

@Composable
internal fun PreviewPage(
    headline: String,
    displayName: String,
    content: String,
    imageUri: Uri?,
    eventLocation: String,
    startDate: OffsetDateTime,
    city: String,
    imageSource: String,
    onSelectImage: (uri: Uri, source: String) -> Unit,
    onClickPost: () -> Unit,
    onClickSaveDraft: () -> Unit,
) {

    var showImagePicker by remember { mutableStateOf(false) }

    if (showImagePicker) {
        ImagePickerSheet(
            defaultSource = imageSource,
            onDismissRequest = { showImagePicker = false },
            onImageSelected = { uri, source ->
                onSelectImage(uri, source)
                showImagePicker = false
            }
        )
    }

    PageTemplate {
        EventCard(
            title = headline,
            content = content.formatToHtml(),
            imageURL = imageUri?.toString(),
            organizationName = displayName,
            date = startDate,
            city = city,
            location = eventLocation,
            onClickPublisher = {},
            onClick = { showImagePicker = true }
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
            PrimaryButton(
                onClick = onClickPost,
                text = stringResource(R.string.event_posting_publish_button_text)
            )
            SecondaryButton(
                onClick = onClickSaveDraft,
                text = stringResource(R.string.event_posting_draft_button_text)
            )
        }
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        PreviewPage(
            headline = "abc",
            content = "abc",
            imageUri = null,
            onSelectImage = { _, _ -> },
            displayName = "",
            eventLocation = "Bremen",
            startDate = OffsetDateTime.now(),
            city = "Bremen",
            imageSource = "",
            onClickPost = { },
            onClickSaveDraft = { }
        )
    }
}