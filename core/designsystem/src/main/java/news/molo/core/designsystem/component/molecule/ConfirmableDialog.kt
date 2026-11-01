package news.molo.core.designsystem.component.molecule

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@Composable
fun ConfirmableDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.MoloOrange
                )
            ) {
                Text("OK")
            }
        },
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ConfirmableDialog(
            title = "Test Title",
            text = "Test text, text text ",
            onConfirm = {},
            onDismiss = {}
        )
    }
}