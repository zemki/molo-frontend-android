package news.molo.core.util.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

@Composable
fun calculatePercentageOfWindowHeight(percentage: Float): Dp {
    return calculatePercentageOfWindowSize(percentage) { it.height }
}

@Composable
fun calculatePercentageOfWindowWidth(percentage: Float): Dp {
    return calculatePercentageOfWindowSize(percentage) { it.width }
}

@Composable
private fun calculatePercentageOfWindowSize(
    percentage: Float,
    dimensionSelector: (IntSize) -> Int
): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return with(density) {
        dimensionSelector(windowInfo.containerSize).toDp() * percentage
    }
}