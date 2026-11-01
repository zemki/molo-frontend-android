package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.atom.BackNavigationIconButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    onNavigateBackClick: () -> Unit
) {

    DefaultTopAppBar(
        modifier = modifier,
        title = {
            title?.let { Text(text = it) }
        },
        actions = actions,
        onNavigateBackClicked = onNavigateBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable RowScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors? = null,
    onNavigateBackClicked: (() -> Unit)? = null
) {
    
    TopAppBar(
        modifier = Modifier.then(modifier),
        colors = colors ?: TopAppBarDefaults.topAppBarColors(
            actionIconContentColor = Color.White,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                title()
            }
        },
        navigationIcon = {
            onNavigateBackClicked?.let { BackNavigationIconButton(onNavigateBack = it) }
        },
        actions = actions
    )

}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        DefaultTopAppBar(
            title = "Title",
            onNavigateBackClick = {}
        )
    }
}