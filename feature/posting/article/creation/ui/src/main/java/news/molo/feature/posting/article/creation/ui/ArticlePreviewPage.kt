package news.molo.feature.posting.article.creation.ui

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
import news.molo.core.designsystem.component.molecule.CommunityArticleCard
import news.molo.core.designsystem.component.molecule.PublishedStyle
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.helper.formatToHtml
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.picker.ImagePickerSheet

@Composable
internal fun ArticlePreviewPage(
    headline: String,
    displayName: String,
    content: String,
    profilePictureUrl: String?,
    imageUri: Uri?,
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
        CommunityArticleCard(
            headline = headline,
            content = content.formatToHtml(),
            imageURL = imageUri?.toString(),
            organizationImageURL = profilePictureUrl ?: "",
            organizationName = displayName,
            publishedStyle = PublishedStyle.Preview,
            onClick = {
                showImagePicker = true
            },
            onPublisherClicked = {},
            isDraft = false
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionGroup(onClickPost, onClickSaveDraft)
    }
}

@Composable
private fun ActionGroup(onClickPost: () -> Unit, onClickSaveDraft: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
        PrimaryButton(
            onClick = onClickPost,
            text = stringResource(R.string.article_posting_publish_button_text)
        )
        SecondaryButton(
            onClick = onClickSaveDraft,
            text = stringResource(R.string.article_posting_draft_button_text)
        )
    }
}


@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        ArticlePreviewPage(
            headline = "abc",
            content = "abc",
            profilePictureUrl = "",
            imageUri = null,
            imageSource = "",
            onSelectImage = { _, _ -> },
            displayName = "",
            onClickPost = {},
            onClickSaveDraft = {},
        )
    }
}