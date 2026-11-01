package news.molo.feature.auth.reset.request.presentation


sealed interface RequestPasswordResetState {

    sealed interface Intent {
        data object NavigateUp: Intent
    }

    data object Loading : RequestPasswordResetState
    data class Default(
        val email: String = "",
        val errorMessage: String? = null,
        val emailRequested: Boolean = false

    ) : RequestPasswordResetState {

        val requestingEnabled: Boolean = email.isNotBlank()

        sealed interface Intent : RequestPasswordResetState.Intent {
            data class SetEmail(val value: String) : Intent
            data object RequestReset : Intent
            data object Retry : Intent
        }
    }
}



