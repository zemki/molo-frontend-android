package news.molo.feature.area.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.molecule.CoveringProgressIndicator
import news.molo.core.designsystem.component.molecule.DefaultTopAppBar
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.ScreenScaffold
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.Area
import news.molo.feature.area.presentation.AreaSelectionState
import news.molo.feature.area.presentation.AreaSelectionViewModel
import news.molo.feature.area.ui.page.AreaSelectionPage

@Composable
fun AreaSelectionScreen(
    viewModel: AreaSelectionViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    AreaSelectionScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent
    )
}

@Composable
private fun AreaSelectionScreenImpl(
    state: AreaSelectionState,
    executeIntent: (AreaSelectionState.Intent) -> Unit
) {

    ScreenScaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.title_area_selection),
                onNavigateBackClick = { executeIntent(AreaSelectionState.Intent.NavigateUp) }
            )
        }
    ) {
        when (state) {
            is AreaSelectionState.Loaded -> AreaSelectionPage(
                query = state.query,
                areas = state.areas,
                onQueryChange = { executeIntent(AreaSelectionState.Loaded.Intent.Query(it)) },
                onAreaClick = {
                    executeIntent(
                        AreaSelectionState.Loaded.Intent.NavigateUpWithResult(
                            it
                        )
                    )
                }
            )

            is AreaSelectionState.Loading -> CoveringProgressIndicator()
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        AreaSelectionScreenImpl(
            state = AreaSelectionState.Loaded(List(4) { index -> Area.mock.copy(id = index + 1) }),
            executeIntent = {}
        )
    }
}