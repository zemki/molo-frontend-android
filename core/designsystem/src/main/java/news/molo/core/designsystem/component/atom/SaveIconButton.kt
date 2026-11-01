package news.molo.core.designsystem.component.atom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun SaveIconButton(
    modifier: Modifier = Modifier,
    onSaveClicked: () -> Unit
) {
    IconButton(
        modifier = Modifier.then(modifier),
        onClick = onSaveClicked
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        SaveIconButton(onSaveClicked = {})
    }
}