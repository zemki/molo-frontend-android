package news.molo.core.designsystem.component.molecule

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    tabCount: Int,
    tabText: @Composable ((index: Int) -> Unit)? = null,
    tabIcon: @Composable ((index: Int) -> Unit)? = null,
    onTabSelected: (index: Int) -> Unit,
) {

    val containerColor = when (MaterialTheme.isDarkTheme) {
        true -> MaterialTheme.colorScheme.surface
        false -> Color.White
    }

    val tabColor = when (MaterialTheme.isDarkTheme) {
        true -> LocalContentColor.current
        false -> Color.Black
    }

    SecondaryTabRow(
        modifier = Modifier.then(modifier),
        containerColor = containerColor,
        selectedTabIndex = selectedTabIndex,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = false),
                color = Color.MoloOrange
            )
        }
    ) {
        for (index in 0 until tabCount) {
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = tabText?.let { { it(index) } },
                icon = tabIcon?.let { { it(index) } },
                selectedContentColor = tabColor,
                unselectedContentColor = tabColor
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        PageIndicator(
            selectedTabIndex = 0,
            tabCount = 2,
            tabText = { index ->
                Text(text = "Tab $index")
            },
            onTabSelected = {}
        )
    }
}