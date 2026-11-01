package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.atom.TextButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@Composable
fun ImageSourceDialog(
    source: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(source) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Bildquelle angeben")
        },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "Bildquelle",
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.MoloOrange
                )
            ) {
                Text("Übernehmen")
            }
        },
        dismissButton = {
            TextButton(text = "Abbrechen", onClick = onDismiss)
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ImageSourceDialog(
            onDismiss = { },
            onConfirm = { }
        )
    }
}
