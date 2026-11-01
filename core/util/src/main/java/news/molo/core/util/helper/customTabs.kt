package news.molo.core.util.helper

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun rememberCustomTabsLauncher(): (String) -> Unit {
    val context = LocalContext.current
    return remember(context) { { url -> launchChromeCustomTab(context, url) } }
}

fun launchChromeCustomTab(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .setShowTitle(true)
        .build()

    try {
        customTabsIntent.launchUrl(context, url.toUri())
    } catch (e: Exception) {
        // Fallback to regular browser if Chrome is not installed
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}