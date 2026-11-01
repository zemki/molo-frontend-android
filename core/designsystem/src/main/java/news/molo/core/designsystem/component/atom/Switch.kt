package news.molo.core.designsystem.component.atom

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        thumbContent = thumbContent,
        colors = SwitchDefaults.colors(
            checkedBorderColor = Color.MoloOrange,
            checkedTrackColor = Color.MoloOrange,
            checkedThumbColor = Color.White
        )
    )
}

@Preview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        Switch(
            checked = true,
            onCheckedChange = { }
        )
    }

}