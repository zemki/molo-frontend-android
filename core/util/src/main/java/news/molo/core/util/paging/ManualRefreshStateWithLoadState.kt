package news.molo.core.util.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.LoadState

class ManualRefreshStateWithLoadState internal constructor(
    private val loadState: State<LoadState>
) {
    private var _isManuallyRefreshing by mutableStateOf(false)

    val isRefreshing: Boolean
        get() = _isManuallyRefreshing && loadState.value is LoadState.Loading

    val showLoading: Boolean
        get() = loadState.value is LoadState.Loading && !_isManuallyRefreshing

    fun start() {
        _isManuallyRefreshing = true
    }

    @Composable
    fun ObserveCompletion() {
        LaunchedEffect(loadState.value) {
            if (_isManuallyRefreshing && loadState.value !is LoadState.Loading) {
                _isManuallyRefreshing = false
            }
        }
    }
}

