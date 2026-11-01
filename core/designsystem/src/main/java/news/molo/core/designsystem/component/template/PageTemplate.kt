package news.molo.core.designsystem.component.template

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.atom.TextField
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
private fun BasePage(
    modifier: Modifier,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    clearFocusOnTouch: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (clearFocusOnTouch) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    }
                } else {
                    Modifier
                }
            )
            .then(modifier),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun PageTemplate(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(Spacing.Medium),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    clearFocusOnTouch: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    BasePage(
        modifier = Modifier
            .padding(padding)
            .then(modifier),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        clearFocusOnTouch = clearFocusOnTouch,
        content = content
    )
}

@Composable
fun PageTemplateWithPaddingSlot(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(Spacing.Medium),
    clearFocusOnTouch: Boolean = true,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.(padding: PaddingValues) -> Unit,
) {
    BasePage(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        clearFocusOnTouch = clearFocusOnTouch
    ) {
        content(padding)
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
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