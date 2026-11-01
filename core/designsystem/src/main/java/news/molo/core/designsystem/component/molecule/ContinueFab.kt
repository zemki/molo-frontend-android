package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun ContinueFab(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .navigationBarsPadding()
            .then(modifier),
        onClick = onClick,
        containerColor = Color.MoloOrange,
        shape = ButtonDefaults.shape,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.padding(vertical = Spacing.Small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = text)
            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
        }
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        ContinueFab(text = "Weiter", onClick = {})
    }
}