package news.molo.android.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import de.convelop.forge.compose.navigation.nav2.HandleNavResults
import de.convelop.forge.compose.navigation.nav2.OnResult
import news.molo.core.navigation.NavigationResult
import news.molo.core.navigation.Screen
import news.molo.feature.area.ui.AreaSelectionScreen
import news.molo.feature.article.detail.ui.DetailedArticleScreen
import news.molo.feature.auth.registration.ui.RegistrationScreen
import news.molo.feature.auth.reset.request.ui.RequestPasswordResetScreen
import news.molo.feature.event.detail.ui.DetailedEventScreen
import news.molo.feature.location.ui.LocationScreen
import news.molo.feature.organization.ui.DetailedOrganizationScreen
import news.molo.feature.posting.article.creation.ui.ArticleCreationScreen
import news.molo.feature.posting.event.creation.presentation.EventCreationNavResult
import news.molo.feature.posting.event.creation.presentation.EventCreationViewModel
import news.molo.feature.posting.event.creation.ui.EventCreationScreen
import news.molo.feature.profile.presentation.ProfileNavResult
import news.molo.feature.profile.presentation.ProfileViewModel
import news.molo.feature.profile.ui.ProfileScreen
import news.molo.feature.search.result.ui.SearchResultScreen
import news.molo.feature.source.ui.OrganizationSummaryScreen
import news.molo.feature.tags.ui.TagSelectionScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.appGraph(navController: NavHostController) {
    composableWithDetailsTransition<Screen.ArticleDetails> {
        val route = it.toRoute<Screen.ArticleDetails>()
        DetailedArticleScreen(
            viewModel = koinViewModel(parameters = {
                parametersOf(route.articleId)
            })
        )
    }

    composableWithDetailsTransition<Screen.EventDetails> {
        val route = it.toRoute<Screen.EventDetails>()
        DetailedEventScreen(
            viewModel = koinViewModel(parameters = {
                parametersOf(route.eventId)
            })
        )
    }

    composableWithDetailsTransition<Screen.Profile> { backStackEntry ->
        val viewModel = koinViewModel<ProfileViewModel>()

        HandleNavResults(
            handle = backStackEntry.savedStateHandle,
            onResult = viewModel::onNavResultReceived
        ) {
            OnResult(
                navResult = NavigationResult.SelectedArea,
                mapper = { area -> ProfileNavResult.SelectedArea(area) }
            )
        }

        ProfileScreen(
            viewModel = viewModel
        )
    }

    composableWithDetailsTransition<Screen.OrganizationSummary> {
        OrganizationSummaryScreen(viewModel = koinViewModel())
    }

    composableWithDetailsTransition<Screen.RequestPasswordReset> {
        RequestPasswordResetScreen(viewModel = koinViewModel())
    }

    composableWithDetailsTransition<Screen.Registration> {
        RegistrationScreen(
            viewModel = koinViewModel()
        )
    }

    composableWithDetailsTransition<Screen.OrganizationDetails> {
        val route = it.toRoute<Screen.OrganizationDetails>()

        DetailedOrganizationScreen(
            viewModel = koinViewModel(parameters = {
                parametersOf(route.organizationId, route.defaultTagSelectionType)
            })
        )
    }

    composableWithDetailsTransition<Screen.AreaSelection> {
        AreaSelectionScreen(viewModel = koinViewModel())
    }

    composableWithDetailsTransition<Screen.TagSelection> {
        val route = it.toRoute<Screen.TagSelection>()
        TagSelectionScreen(
            viewModel = koinViewModel(parameters = {
                parametersOf(route.defaultTagSelectionType, route.tagConfigurationType)
            })
        )
    }

    composableWithDetailsTransition<Screen.LocationSelection> {
        val route = it.toRoute<Screen.LocationSelection>()

        val initialLatLng: Pair<Double, Double>? =
            if (route.initialLongitude == null || route.initialLatitude == null) {
                null
            } else {
                (route.initialLatitude ?: 0.0) to (route.initialLongitude ?: 0.0)
            }

        LocationScreen(
            configurationType = route.configurationType,
            initialLatLng = initialLatLng,
            viewModel = koinViewModel(parameters = {
                parametersOf(route.configurationType)
            })
        )
    }

    composableWithDetailsTransition<Screen.ArticleCreation> {
        val route = it.toRoute<Screen.ArticleCreation>()
        ArticleCreationScreen(viewModel = koinViewModel(parameters = {
            parametersOf(route.articleId)
        }))
    }

    composableWithDetailsTransition<Screen.EventCreation> { backStackEntry ->
        val route = backStackEntry.toRoute<Screen.EventCreation>()
        val viewModel = koinViewModel<EventCreationViewModel>(parameters = {
            parametersOf(
                route.eventId
            )
        })

        HandleNavResults(
            handle = backStackEntry.savedStateHandle,
            onResult = viewModel::onNavResultReceived
        ) {
            OnResult(
                navResult = NavigationResult.LocationSelected,
                mapper = { location -> EventCreationNavResult.Location(location) }
            )
        }

        EventCreationScreen(
            viewModel = viewModel
        )
    }

    composableWithDetailsTransition<Screen.SearchResults> {
        val route = it.toRoute<Screen.SearchResults>()
        SearchResultScreen(viewModel = koinViewModel(parameters = {
            parametersOf(
                route.query,
                route.initialTabSelectionType
            )
        }))
    }
}
