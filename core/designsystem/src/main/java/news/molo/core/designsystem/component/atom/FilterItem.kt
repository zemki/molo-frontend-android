package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.preview.ComponentPreview

@Composable
fun FilterItem(
    modifier: Modifier = Modifier,
    title: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit
) {

    val selectedContainerColor = when(MaterialTheme.isDarkTheme) {
        true -> Color.White
        false -> Color(0xFFF58330)
    }
    val selectedContentColor = when(MaterialTheme.isDarkTheme) {
        true -> Color.Black
        false -> Color.White
    }

    FilterChip(
        modifier = Modifier.then(modifier),
        onClick = { onSelectedChange(!selected) },
        label = { Text(title, maxLines = 1) },
        selected = selected,
        leadingIcon = leadingIcon,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = selectedContainerColor,
            selectedLabelColor = selectedContentColor,
            selectedLeadingIconColor = selectedContentColor,
            iconColor = Color.MoloOrange
        )
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        var selected by remember { mutableStateOf(true) }

        FilterItem(
            selected = selected,
            onSelectedChange = { selected = it },
            title = "Musik & Konzerte",
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(Spacing.Medium),
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null
                )
            }
        )
    }
}
