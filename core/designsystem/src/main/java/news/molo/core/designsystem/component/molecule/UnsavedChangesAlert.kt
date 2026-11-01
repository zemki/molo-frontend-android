package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.SmallPrimaryButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun UnsavedChangesAlert(
    onDismiss: () -> Unit,
    onConfirmSave: () -> Unit,
    onDiscard: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Ungespeicherte Änderungen")
        },
        text = {
            Text(text = "Du hast ungespeicherte Änderungen. Wenn du jetzt zurückgehst, geht dein Fortschritt verloren.")
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallPrimaryButton(
                    onClick = onConfirmSave,
                    modifier = Modifier.fillMaxWidth(),
                    text = "Entwurf Speichern"
                )

                OutlinedButton(
                    onClick = onDiscard,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Änderungen verwerfen")
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abbrechen")
                }
            }
        },
        dismissButton = {}
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        UnsavedChangesAlert(
            onDismiss = { },
            onConfirmSave = { },
            onDiscard = { }
        )
    }
}
