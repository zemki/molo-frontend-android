package news.molo.core.designsystem.component.atom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
fun BackNavigationIconButton(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    IconButton(
        modifier = Modifier.then(modifier),
        onClick = onNavigateBack
    ) {
        Icon(
            painter = painterResource(R.drawable.icn_arrow_back),
            contentDescription = null
        )
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        BackNavigationIconButton {}
    }
}