package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.preview.ComponentPreview

@Composable
fun <T> BottomNavigationBar(
    items: List<T>,
    selectedItem: T,
    icon: @Composable RowScope.(item: T) -> Unit,
    label: @Composable RowScope.(item: T) -> Unit,
    onItemSelected: (T) -> Unit,
) {

    val containerColor = when (MaterialTheme.isDarkTheme) {
        true -> NavigationBarDefaults.containerColor
        false -> Color(0xFFF58330)
    }

    val indicatorColor = when (MaterialTheme.isDarkTheme) {
        true -> Color.White.copy(alpha = 0.25f)
        false -> Color.White.copy(alpha = 0.25f)
    }

    NavigationBar(
        containerColor = containerColor,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    icon(item)
                },
                label = {
                    label(item)
                },
                selected = item == selectedItem,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = indicatorColor,
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,
                    unselectedIconColor = Color.White
                ),
                onClick = {
                    onItemSelected(item)
                }
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    data class BottomNavItem(
        val title: String,
        val icon: ImageVector,
    )

    val items = listOf(
        BottomNavItem(
            title = "Tab 1",
            icon = Icons.Default.Dashboard
        ),
        BottomNavItem(
            title = "Tab #2",
            icon = Icons.Default.Done
        )
    )

    ComponentPreviewTemplate {
        BottomNavigationBar(
            items = items,
            selectedItem = items.first(),
            icon = {
                Icon(imageVector = it.icon, contentDescription = null)
            },
            label = {
                Text(text = it.title)
            },
            onItemSelected = {}
        )
    }
}