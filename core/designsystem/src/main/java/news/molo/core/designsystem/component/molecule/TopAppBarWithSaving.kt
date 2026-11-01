package news.molo.core.designsystem.component.molecule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.SaveIconButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun TopAppBarWithSaving(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateBackClicked: () -> Unit,
    isSavingEnabled: Boolean,
    onSaveClicked: () -> Unit
) {
    DefaultTopAppBar(
        modifier = modifier,
        title = title,
        onNavigateBackClick = onNavigateBackClicked,
        actions = {
            AnimatedVisibility(isSavingEnabled) {
                SaveIconButton(onSaveClicked = onSaveClicked)
            }
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        TopAppBarWithSaving(
            title = "Title",
            isSavingEnabled = true,
            onNavigateBackClicked = {},
            onSaveClicked = {}
        )
    }
}