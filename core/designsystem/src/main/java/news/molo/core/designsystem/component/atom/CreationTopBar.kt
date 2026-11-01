package news.molo.core.designsystem.component.atom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.LocalIsDarkTheme
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationTopBar(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {

    val isDarkTheme = LocalIsDarkTheme.current
    val buttonContainerColor = remember (isDarkTheme){
        when(isDarkTheme) {
            true -> Color.MoloOrange
            false -> Color.White
        }
    }

    val buttonContentColor = remember (isDarkTheme){
        when(isDarkTheme) {
            true -> Color.White
            false -> Color.MoloOrange
        }
    }

    DefaultTopAppBar(
        onNavigateBackClick = onBackClick,
        actions = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonContentColor
                ),
                onClick = onNextClick
            ) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        CreationTopBar({}) { }
    }
}