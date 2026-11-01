package news.molo.feature.picker

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import news.molo.core.designsystem.component.molecule.ImageSourceDialog
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerSheet(
    defaultSource: String = "",
    onDismissRequest: () -> Unit,
    onImageSelected: (Uri, String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    var showSourceDialog by remember { mutableStateOf(false) }
    var tempUri: Uri? by remember { mutableStateOf(Uri.EMPTY) }
    var pendingUri by remember { mutableStateOf(Uri.EMPTY) }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                pendingUri = it
                showSourceDialog = true
            }
        }
    )

    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                pendingUri = tempUri
                showSourceDialog = true
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = context.createTempPictureUri()
            tempUri = uri
            takePhoto.launch(uri)
        } else {
            Toast.makeText(context, "Zugriff auf Kamera verweigert.", Toast.LENGTH_SHORT).show()
        }
    }

    if (!showSourceDialog) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.photo_picker_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.photo_picker_gallery_title)) },
                    leadingContent = { Icon(Icons.Rounded.PhotoLibrary, null) },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    modifier = Modifier.clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )

                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.photo_picker_camera_title)) },
                    leadingContent = { Icon(Icons.Rounded.PhotoCamera, null) },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    modifier = Modifier.clickable {
                        val permission = android.Manifest.permission.CAMERA
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            val uri = context.createTempPictureUri()
                            tempUri = uri
                            takePhoto.launch(uri)
                        } else {
                            cameraPermissionLauncher.launch(permission)
                        }
                    }
                )
            }
        }
    }

    if (showSourceDialog) {
        ImageSourceDialog(
            source = defaultSource,
            onDismiss = {
                showSourceDialog = false
            },
            onConfirm = { sourceInput ->
                onImageSelected(pendingUri, sourceInput)
                showSourceDialog = false
            }
        )
    }
}

fun Context.createTempPictureUri(): Uri {
    val tempFile = File.createTempFile(
        "picture_${System.currentTimeMillis()}",
        ".jpg",
        cacheDir
    ).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        this,
        "news.molo.android.provider",
        tempFile
    )
}

@PreviewScreenSizes
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ImagePickerSheet("", {}) {_, _ -> }
    }
}