package news.molo.core.designsystem.component.atom

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewTopBar(
    onNavigateBack: () -> Unit,
    onPost: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            TextButton(
                onClick = onNavigateBack,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
            ) {
                Text("Abbrechen")
            }
        },
        title = {},
        actions = {
            FilledIconButton(
                onClick = { /* TODO: Implement edit functionality */ },
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF3E3E3E)
                )
            ) {
                Icon(
                    painterResource(R.drawable.icn_edit_posting),
                    contentDescription = null
                )
            }
            Button(
                onClick = onPost,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.MoloOrange,
                    contentColor = Color.White
                ),
            ) {
                Text("Post")
            }
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        PreviewTopBar({}) { }
    }
}