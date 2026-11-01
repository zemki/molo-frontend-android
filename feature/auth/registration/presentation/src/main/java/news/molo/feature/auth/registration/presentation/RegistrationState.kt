package news.molo.feature.auth.registration.presentation

sealed interface RegistrationState {
    data object Loading : RegistrationState

    sealed interface Intent {
        data object NavigateUp : Default.Intent
    }

    data class Default(
        val email: String = "",
        val password: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val displayName: String = "",
        val agbAccepted: Boolean = false,
        val errorMessage: String? = null
    ) : RegistrationState {
        sealed interface Intent : RegistrationState.Intent {
            data class SetFirstName(val value: String) : Intent
            data class SetLastName(val value: String) : Intent
            data class SetDisplayName(val value: String) : Intent
            data class SetEmail(val value: String) : Intent
            data class SetPassword(val value: String) : Intent
            data class SetAgbAccepted(val value: Boolean) : Intent
            data object Register : Intent
            data object Retry : Intent
        }

        val allFieldsFilled: Boolean
            get() {
                return arrayOf(email, password, firstName, lastName, displayName).all { it.isNotBlank() } && agbAccepted
            }
    }

    data object Done: RegistrationState
}

