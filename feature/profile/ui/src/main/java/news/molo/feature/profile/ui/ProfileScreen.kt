package news.molo.feature.profile.ui

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.flow.SharedFlow
import news.molo.core.designsystem.component.atom.SmallPrimaryButton
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.User
import news.molo.feature.profile.presentation.ProfileState
import news.molo.feature.profile.presentation.ProfileViewModel
import news.molo.feature.profile.ui.page.ProfilePage

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    ProfileScreenImpl(
        state = state,
        eventFlow = viewModel.events,
        executeIntent = viewModel::executeIntent
    )
}

@Composable
private fun ProfileScreenImpl(
    state: ProfileState,
    eventFlow: SharedFlow<UIEvent>,
    executeIntent: (ProfileState.Intent) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }

    UIEventHandler(
        uiEvent = eventFlow,
        snackBarHostState,
    )

    ScreenScaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.title_profile),
                onNavigateBackClick = { executeIntent(ProfileState.Intent.NavigateUp) },
                actions = {
                    SmallPrimaryButton(
                        text = "Speichern",
                        onClick = { executeIntent(ProfileState.Loaded.Intent.SaveChanges) },
                        enabled = state is ProfileState.Loaded && state.hasChanges
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) {

        when (state) {
            is ProfileState.Loaded -> ProfilePage(
                user = state.user,
                onFirstNameChange = { executeIntent(ProfileState.Loaded.Intent.SetFirstName(it)) },
                onLastNameChange = { executeIntent(ProfileState.Loaded.Intent.SetLastName(it)) },
                onDisplayNameChange = { executeIntent(ProfileState.Loaded.Intent.SetDisplayName(it)) },
                onDescriptionChange = { executeIntent(ProfileState.Loaded.Intent.SetDescription(it)) },
                onAreaClick = { executeIntent(ProfileState.Loaded.Intent.NavigateToAreaSelection) },
                onPhoneChange = { executeIntent(ProfileState.Loaded.Intent.SetPhone(it)) },
                onMailChange = { executeIntent(ProfileState.Loaded.Intent.SetMail(it)) },
                onHomepageChange = { executeIntent(ProfileState.Loaded.Intent.SetHomepage(it)) },
                onSelectPicture = { uri, source ->
                    executeIntent(
                        ProfileState.Loaded.Intent.SetImage(
                            uri,
                            source
                        )
                    )
                }
            )

            is ProfileState.Loading -> CoveringProgressIndicator()
        }
    }
}

private class Provider : PreviewParameterProvider<ProfileState> {
    override val values = sequenceOf(
        ProfileState.Loaded(User.mock, User.mock),
        ProfileState.Loading
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: ProfileState
) {

    ComponentPreviewTemplate {
        ProfileScreenImpl(
            state = state,
            eventFlow = UIEvent.mock,
            executeIntent = {}
        )
    }
}