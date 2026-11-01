package news.molo.android.presentation

sealed class BottomSheetState {
    object Hidden : BottomSheetState()
    object Login : BottomSheetState()
    data class ResetPassword(val token: String): BottomSheetState()
}