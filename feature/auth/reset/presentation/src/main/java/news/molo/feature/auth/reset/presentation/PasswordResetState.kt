package news.molo.feature.auth.reset.presentation


sealed interface PasswordResetState {

    sealed interface Intent {
        data object NavigateUp : Intent
    }

    data object Loading : PasswordResetState
    data class Default(
        val password: String = "",
        val repeatPassword: String = "",
        val errorMessage: String? = null,
        val emailRequested: Boolean = false
    ) : PasswordResetState {

        val doesPasswordMatch get() = password == repeatPassword && password.isNotBlank()

        sealed interface Intent : PasswordResetState.Intent {
            data class SetPassword(val value: String) : Intent
            data class SetRepeatPassword(val value: String) : Intent

            data object RequestReset : Intent
            data object Retry : Intent
        }
    }
}



