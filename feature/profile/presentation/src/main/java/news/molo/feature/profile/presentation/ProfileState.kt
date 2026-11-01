package news.molo.feature.profile.presentation

import news.molo.domain.model.User

sealed interface ProfileState {
    sealed interface Intent {
        data object NavigateUp: Intent
    }

    data object Loading : ProfileState

    data class Loaded(
        val originalUser: User,
        val user: User
    ) : ProfileState {

        val hasChanges: Boolean
            get() = user != originalUser

        sealed interface Intent : ProfileState.Intent {
            data class SetFirstName(val value: String) : Intent
            data class SetLastName(val value: String) : Intent
            data class SetDisplayName(val value: String) : Intent
            data class SetDescription(val value: String) : Intent
            data class SetPhone(val value: String) : Intent
            data class SetMail(val value: String) : Intent
            data class SetHomepage(val value: String) : Intent
            data class SetImage(val value: String, val source: String) : Intent
            data object SaveChanges : Intent

            data object NavigateToAreaSelection : Intent
        }
    }
}