package news.molo.core.designsystem.component.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun SpacedGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        content = content
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        SpacedGroup {
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
        }
    }
}