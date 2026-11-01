package news.molo.feature.profile.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ClickableTextField
import news.molo.core.designsystem.component.atom.ShapedImage
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.component.template.SpacedGroup
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.User
import news.molo.feature.picker.ImagePickerSheet

@Composable
internal fun ProfilePage(
    user: User,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAreaClick: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onMailChange: (String) -> Unit,
    onHomepageChange: (String) -> Unit,
    onSelectPicture: (uriString: String, source: String) -> Unit,
) {

    var showImagePicker by remember { mutableStateOf(false) }

    if (showImagePicker) {
        ImagePickerSheet(
            defaultSource = user.imageSource.orEmpty(),
            onDismissRequest = { showImagePicker = false },
            onImageSelected = { uri, source ->
                onSelectPicture(uri.toString(), source)
                showImagePicker = false
            }
        )
    }

    PageTemplate(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        SpacedGroup(modifier = Modifier.fillMaxWidth()) {
            ProfilePictureWithSource(
                imageUrl = user.imageUrl,
                imageSource = user.imageSource,
                onPictureClick = { showImagePicker = true }
            )

            PublicInformationFields(
                firstName = user.firstName,
                lastName = user.lastName,
                displayName = user.displayName,
                description = user.description,
                area = user.area.name,
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange,
                onDisplayNameChange = onDisplayNameChange,
                onDescriptionChange = onDescriptionChange,
                onAreaClick = onAreaClick
            )

            PrivateInformationFields(
                phone = user.phone,
                mail = user.mail,
                homepage = user.homepage,
                onPhoneChange = onPhoneChange,
                onMailChange = onMailChange,
                onHomepageChange = onHomepageChange
            )
        }
    }
}

@Composable
private fun PublicInformationFields(
    firstName: String,
    lastName: String,
    displayName: String,
    description: String,
    area: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAreaClick: () -> Unit
) {
    Text(
        text = stringResource(R.string.profile_public_information_title),
        style = MaterialTheme.typography.titleSmall
    )

    TextField(
        value = firstName,
        placeholder = stringResource(R.string.profile_first_name_hint),
        onValueChange = onFirstNameChange,
        contentType = ContentType.PersonFirstName,
    )

    TextField(
        value = lastName,
        placeholder = stringResource(R.string.profile_last_name_hint),
        onValueChange = onLastNameChange,
        contentType = ContentType.PersonLastName,
    )

    TextField(
        value = displayName,
        placeholder = stringResource(R.string.profile_display_name_hint),
        onValueChange = onDisplayNameChange,
        contentType = ContentType.Username,
    )

    TextField(
        value = description,
        placeholder = stringResource(R.string.profile_description_hint),
        onValueChange = onDescriptionChange,
        isSingleLine = false,
        minLines = 3,
        maxLines = 3,
        keyboardType = KeyboardType.Text
    )

    ClickableTextField(
        value = area,
        placeholder = stringResource(R.string.profile_area_hint),
        onClick = onAreaClick
    )
}

@Composable
private fun PrivateInformationFields(
    phone: String,
    mail: String,
    homepage: String,
    onPhoneChange: (String) -> Unit,
    onMailChange: (String) -> Unit,
    onHomepageChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.profile_private_information_title),
        style = MaterialTheme.typography.titleSmall
    )

    TextField(
        value = phone,
        placeholder = stringResource(R.string.profile_phone_hint),
        onValueChange = onPhoneChange,
        keyboardType = KeyboardType.Phone,
        contentType = ContentType.PhoneNumber,
    )

    TextField(
        value = mail,
        placeholder = stringResource(R.string.profile_mail_hint),
        onValueChange = onMailChange,
        keyboardType = KeyboardType.Email,
        contentType = ContentType.EmailAddress,
    )

    TextField(
        value = homepage,
        placeholder = stringResource(R.string.profile_homepage_hint),
        onValueChange = onHomepageChange,
        keyboardType = KeyboardType.Uri,
        imeAction = ImeAction.Done,
    )
}

@Composable
private fun ProfilePictureWithSource(
    imageUrl: String?,
    imageSource: String?,
    onPictureClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EditablePicture(imageUrl, onPictureClick)

        if (!imageSource.isNullOrBlank()) {
            ImageSourceText(imageSource)
        }
    }
}

@Composable
private fun ColumnScope.EditablePicture(
    imageUrl: String?,
    onPictureClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .align(Alignment.CenterHorizontally),
        contentAlignment = Alignment.Center
    ) {
        ShapedImage(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onPictureClick),
            imageUrl = imageUrl,
        )

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier
                .padding(Spacing.Medium)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun ColumnScope.ImageSourceText(source: String) {
    Text(
        text = "© $source",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.End,
        modifier = Modifier
            .padding(
                vertical = Spacing.ExtraSmall,
                horizontal = Spacing.Medium,
            )
            .width(200.dp)
            .align(Alignment.CenterHorizontally)
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ProfilePage(
            user = User.mock,
            onFirstNameChange = {},
            onLastNameChange = {},
            onDisplayNameChange = {},
            onDescriptionChange = {},
            onAreaClick = {},
            onPhoneChange = {},
            onMailChange = {},
            onHomepageChange = {},
            onSelectPicture = { _, _ -> },
        )
    }
}