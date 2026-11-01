package news.molo.android.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlin.reflect.KType

internal inline fun <reified T : Any> NavGraphBuilder.composableWithDetailsTransition(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = DefaultNavTransitions.enterTransition,
        popEnterTransition = DefaultNavTransitions.popEnterTransition,
        exitTransition = DefaultNavTransitions.exitTransition,
        popExitTransition = DefaultNavTransitions.popExitTransition,
        content = content,
    )
}

@PublishedApi
internal object DefaultNavTransitions {
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start)
        }
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                targetOffset = { fullOffset -> (fullOffset * 0.3f).toInt() },
            )
        }
    val popEnterTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec =
                tween(
                    durationMillis = 150,
                    easing = LinearEasing,
                ),
            initialOffset = { fullOffset -> (fullOffset * 0.3f).toInt() },
        )
    }
    val popExitTransition:
            AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec =
                tween(
                    durationMillis = 150,
                    easing = LinearEasing,
                ),
        )
    }
}
