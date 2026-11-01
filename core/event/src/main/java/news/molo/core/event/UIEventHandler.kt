package news.molo.core.event

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun UIEventHandler(
    uiEvent: Flow<UIEvent>,
    snackBarHostState: SnackbarHostState? = null,
    onNavigateUpRequested: () -> Unit = {},
    onEventHandled: (UIEvent) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UIEvent.SnackBar -> {
                    coroutineScope.launch {
                        snackBarHostState?.showSnackbar(
                            message = event.message,
                        )
                    }
                }

                UIEvent.NavigateBack -> {
                    onNavigateUpRequested()
                }

                else -> {}
            }
            onEventHandled(event)
        }
    }
}