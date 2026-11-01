package news.molo.core.designsystem.component.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.molecule.ConfirmableDialog
import news.molo.core.util.preview.ComponentPreview

@Composable
fun DialogHolderTemplate(
    modifier: Modifier = Modifier,
    isDialogVisible: Boolean,
    dialog: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    Column(modifier = Modifier.then(modifier)) {
        if (isDialogVisible) {
            dialog()
        }

        content()
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        DialogHolderTemplate(
            isDialogVisible = true,
            dialog = {
                ConfirmableDialog(
                    title = "Dialog Example",
                    text = "Thats a exmaple dialog",
                    onConfirm = {},
                    onDismiss = {}
                )
            },
            content = {
                PageTemplate {

                    Text(text = "Thats a page")
                    TextField(
                        value = "",
                        placeholder = "Test #1",
                        onValueChange = {}
                    )

                    TextField(
                        value = "",
                        placeholder = "Test #2",
                        onValueChange = {}
                    )

                    TextField(
                        value = "",
                        placeholder = "Test #3",
                        onValueChange = {}
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(onClick = {}, text = "Some Action")
                }
            }
        )
    }
}