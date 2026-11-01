package news.molo.core.util.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "default - light",
    group = "standard",
    backgroundColor = 0x000000,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
internal annotation class ComponentPreviewLight

@Preview(
    name = "default - dark",
    group = "standard",
    backgroundColor = 0xFF121212,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
internal annotation class ComponentPreviewDark

@ComponentPreviewLight
@ComponentPreviewDark
annotation class ComponentPreview