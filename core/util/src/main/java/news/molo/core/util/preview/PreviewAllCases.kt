package news.molo.core.util.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "default - light",
    group = "standard",
    widthDp = 375,
    heightDp = 852,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
annotation class PreviewDefaultLight

@Preview(
    name = "default - dark",
    group = "standard",
    widthDp = 375,
    heightDp = 852,
    showBackground = true,
    backgroundColor = 0xFF121212,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class PreviewDefaultDark

@Preview(
    name = "worst case - light",
    group = "standard",
    widthDp = 320,
    heightDp = 592,
    fontScale = 1.5f,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
annotation class PreviewWorstCaseLight

@Preview(
    name = "worst case - dark",
    group = "standard",
    widthDp = 320,
    heightDp = 592,
    fontScale = 1.5f,
    showBackground = true,
    backgroundColor = 0xFF121212,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class PreviewWorstCaseDark

@PreviewDefaultLight
@PreviewDefaultDark
@PreviewWorstCaseLight
@PreviewWorstCaseDark
annotation class PreviewAllCases