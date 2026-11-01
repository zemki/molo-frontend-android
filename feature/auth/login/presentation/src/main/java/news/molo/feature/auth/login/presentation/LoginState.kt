package news.molo.feature.auth.login.presentation

sealed interface LoginState {
    sealed interface Intent {
        data object ToRegistration : Intent
        data object ToPasswordReset : Intent
    }

    data object Loading : LoginState

    data class Default(
        val email: String = "",
        val password: String = "",
        val errorMessage: String? = null
    ) : LoginState {
        sealed interface Intent : LoginState.Intent {
            data class SetEmail(val value: String) : Intent
            data class SetPassword(val value: String) : Intent
            data object Login : Intent
            data object Retry : Intent
        }
    }
}