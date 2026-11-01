package news.molo.core.designsystem.component.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.util.preview.ComponentPreview

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.then(modifier),
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentWindowInsets = contentWindowInsets
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ScreenScaffold(
            topBar = {
                DefaultTopAppBar(
                    title = "Title",
                    onNavigateBackClick = {}
                )
            }
        ) {
            PageTemplate {

                Text(text = "Thats a page")

                TextField(
                    value = "",
                    placeholder = "Test #1",
                    onValueChange = {}
                )

                TextField(
                    value = "",
                    placeholder = "Test #2",
                    onValueChange = {}
                )

                TextField(
                    value = "",
                    placeholder = "Test #3",
                    onValueChange = {}
                )

                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(onClick = {}, text = "Some Action")
            }
        }
    }
}