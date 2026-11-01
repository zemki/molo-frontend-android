package news.molo.android.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import news.molo.android.navigation.AppNavigation
import news.molo.android.presentation.AppState
import news.molo.android.presentation.MainViewModel
import news.molo.core.designsystem.theme.LocalIsDarkTheme
import news.molo.domain.model.ColorSchemaPreference
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        processIntent(intent)

        setContent {
            val state by viewModel.collectStateFlow().collectAsState()

            splashScreen.setKeepOnScreenCondition {
                state.colorSchemaPreference == null || state.onboardingCompletionState == null
            }

            val colorSchema = state.colorSchemaPreference
            val onboardingState = state.onboardingCompletionState

            if (colorSchema != null && onboardingState != null) {
                CompositionLocalProvider(LocalIsDarkTheme provides colorSchema.isDarkTheme()) {
                    AppNavigation(
                        state = state,
                        executeIntent = viewModel::executeIntent
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {


        val extras = intent.extras ?: return

        val articleId = extras.getString("article_id")
        val eventId = extras.getString("event_id")

        if (!articleId.isNullOrEmpty()) {
            intent.removeExtra("article_id")
            viewModel.executeIntent(AppState.Intent.NavigateToArticle(articleId.toInt()))
        } else if (!eventId.isNullOrEmpty()) {
            intent.removeExtra("event_id")
            viewModel.executeIntent(AppState.Intent.NavigateToEvent(eventId.toInt()))
        }
    }
}

@Composable
private fun ColorSchemaPreference.isDarkTheme(): Boolean = when (this) {
    ColorSchemaPreference.Dark -> true
    ColorSchemaPreference.Light -> false
    ColorSchemaPreference.System -> isSystemInDarkTheme()
}

