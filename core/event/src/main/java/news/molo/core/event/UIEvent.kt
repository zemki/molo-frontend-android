package news.molo.core.event

import kotlinx.coroutines.flow.MutableStateFlow

sealed interface UIEvent {
    data class SnackBar(val message: String) : UIEvent
    data object NavigateBack : UIEvent
    data object RequestNotificationPermission : UIEvent

    companion object {
        val mock = MutableStateFlow(SnackBar("Mock message"))
    }
}
