package news.molo.feature.auth.registration.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.feature.auth.registration.presentation.RegistrationState
import news.molo.feature.auth.registration.presentation.RegistrationViewModel
import news.molo.feature.auth.registration.ui.page.RegistrationDonePage
import news.molo.feature.auth.registration.ui.page.RegistrationPage

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    RegistrationScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationScreenImpl(
    state: RegistrationState,
    executeIntent: (RegistrationState.Intent) -> Unit,
) {

    ScreenScaffold(
        topBar = {
            RegistrationTopBar(
                state = state,
                onClickNavigateUp = { executeIntent(RegistrationState.Intent.NavigateUp) }
            )
        }
    ) {

        when (state) {
            is RegistrationState.Default -> RegistrationPage(
                mail = state.email,
                password = state.password,
                firstName = state.firstName,
                lastName = state.lastName,
                displayName = state.displayName,
                agbAccepted = state.agbAccepted,
                errorMessage = state.errorMessage,
                enableRegistration = state.allFieldsFilled,
                onConfirmErrorDialogClick = { executeIntent(RegistrationState.Default.Intent.Retry) },
                onMailChange = { executeIntent(RegistrationState.Default.Intent.SetEmail(it)) },
                onPasswordChange = { executeIntent(RegistrationState.Default.Intent.SetPassword(it)) },
                onFirstNameChange = { executeIntent(RegistrationState.Default.Intent.SetFirstName(it)) },
                onLastNameChange = { executeIntent(RegistrationState.Default.Intent.SetLastName(it)) },
                onDisplayNameChange = {
                    executeIntent(
                        RegistrationState.Default.Intent.SetDisplayName(
                            it
                        )
                    )
                },
                onAgbAcceptedChange = {
                    executeIntent(
                        RegistrationState.Default.Intent.SetAgbAccepted(
                            it
                        )
                    )
                },
                onClickRegister = { executeIntent(RegistrationState.Default.Intent.Register) }
            )

            RegistrationState.Loading -> CoveringProgressIndicator()
            RegistrationState.Done -> RegistrationDonePage {
                executeIntent(RegistrationState.Intent.NavigateUp)
            }
        }
    }
}

@Composable
private fun RegistrationTopBar(state: RegistrationState, onClickNavigateUp: () -> Unit) {
    if (state !is RegistrationState.Done) {
        DefaultTopAppBar(
            onNavigateBackClick = onClickNavigateUp
        )
    }
}
