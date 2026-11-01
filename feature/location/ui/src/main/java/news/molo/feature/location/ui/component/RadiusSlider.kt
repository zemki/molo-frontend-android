package news.molo.feature.location.ui.component

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange

@Composable
internal fun RadiusSlider(
    radiusInKm: Int,
    onRadiusChange: (Int) -> Unit,
) {
    val maxRadius = 200f
    val stepValue = 5

    val stepsCount = ((maxRadius / stepValue) - 1).toInt()


    Slider(
        value = if (radiusInKm == 1) 0f else radiusInKm.toFloat(),
        onValueChange = { value ->
            val intValue = value.toInt()
            onRadiusChange(if (intValue == 0) 1 else intValue)
        },
        colors = SliderDefaults.colors(
            thumbColor = Color.MoloOrange,
            activeTrackColor = Color.MoloOrange,
            activeTickColor = Color.MoloOrange
        ),
        valueRange = 0f..maxRadius,
        steps = stepsCount
    )
}

@Preview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        RadiusSlider(10, {})
    }
}
