package news.molo.feature.source.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.SharedFlow
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.event.UIEvent
import news.molo.core.event.UIEventHandler
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.OrganizationSummary
import news.molo.feature.source.presentation.OrganizationSummaryState
import news.molo.feature.source.presentation.OrganizationSummaryViewModel
import news.molo.feature.source.ui.page.OrganizationSummaryPage

@Composable
fun OrganizationSummaryScreen(
    viewModel: OrganizationSummaryViewModel,
) {
    val state by viewModel.collectStateFlow().collectAsState()

    OrganizationSummaryScreenImpl(
        state = state,
        uiEvents = viewModel.events,
        executeIntent = viewModel::executeIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrganizationSummaryScreenImpl(
    state: OrganizationSummaryState,
    uiEvents: SharedFlow<UIEvent>,
    executeIntent: (OrganizationSummaryState.Intent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    UIEventHandler(
        uiEvent = uiEvents,
        snackBarHostState = snackBarHostState
    )

    ScreenScaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.title_my_sources),
                onNavigateBackClick = { executeIntent(OrganizationSummaryState.Intent.NavigateUp) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        when (state) {
            is OrganizationSummaryState.Loaded -> OrganizationSummaryPage(
                summaries = state.summaries,
                expandedSummaryNames = state.expandedSummaryNames,
                onSummaryClick = {
                    executeIntent(OrganizationSummaryState.Loaded.Intent.ToggleSummaryExpansion(it))
                },
                onToggleFollowingClick = {
                    executeIntent(
                        OrganizationSummaryState.Loaded.Intent.ToggleOrganizationSelection(
                            it
                        )
                    )
                },
                onOrganisationClick = {
                    executeIntent(
                        OrganizationSummaryState.Loaded.Intent.NavigateToOrganizationDetails(
                            it.id
                        )
                    )
                }
            )

            is OrganizationSummaryState.Loading -> CoveringProgressIndicator()
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = OrganizationSummaryState.Loaded(
        List(3) { _ ->
            OrganizationSummary.mock
        }
    )

    ComponentPreviewTemplate {
        OrganizationSummaryScreenImpl(
            state = state,
            uiEvents = UIEvent.mock,
            executeIntent = {}
        )
    }
}