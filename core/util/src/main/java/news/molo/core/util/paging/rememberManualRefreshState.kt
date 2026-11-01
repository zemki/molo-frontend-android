package news.molo.core.util.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.paging.LoadState

@Composable
fun rememberManualRefreshState(loadState: LoadState): ManualRefreshStateWithLoadState {
    val state = rememberUpdatedState(loadState)

    return remember { ManualRefreshStateWithLoadState(state) }
        .also { it.ObserveCompletion() }
}