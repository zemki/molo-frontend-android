package news.molo.feature.onboarding.ui


import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.molecule.AnimatedCircularProgressIndicator
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.event.UIEvent
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.onboarding.presentation.OnboardingState
import news.molo.feature.onboarding.presentation.OnboardingViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel
) {
    val state by viewModel.collectStateFlow().collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.executeIntent(OnboardingState.Intent.PermissionResult(it))
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is UIEvent.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.executeIntent(OnboardingState.Intent.Next)
                    }
                }

                else -> {}
            }
        }
    }

    OnboardingScreenImpl(
        state = state,
        executeIntent = viewModel::executeIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OnboardingScreenImpl(
    state: OnboardingState,
    executeIntent: (OnboardingState.Intent) -> Unit,
) {

    BackHandler(!state.isFirstStep) { executeIntent(OnboardingState.Intent.Previous) }

    Scaffold(
        topBar = {
            OnboardingTopBar(
                isNavigationIconVisible = !state.isFirstStep,
                onNavigationIconClick = { executeIntent(OnboardingState.Intent.Previous) },
                isProgressVisible = state.isProgressVisible,
                progress = state.progress,
                currentStep = state.currentStepNumber,
                totalSteps = state.totalSteps
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            PrimaryButton(
                text = stringResource(state.titleResForStep),
                modifier = Modifier.padding(Spacing.Medium),
                onClick = { executeIntent(OnboardingState.Intent.Next) }
            )
        }
    ) { _ ->
        OnboardingPage(
            state = state,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingTopBar(
    isNavigationIconVisible: Boolean,
    onNavigationIconClick: () -> Unit,
    isProgressVisible: Boolean,
    progress: Float, currentStep: Int, totalSteps: Int
) {
    TopAppBar(
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            AnimatedVisibility(isNavigationIconVisible) {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                }
            }
        },
        actions = {
            AnimatedVisibility(isProgressVisible) {
                OnboardingProgressIndicator(
                    progress = progress,
                    currentStep = currentStep,
                    totalSteps = totalSteps
                )
            }
        }
    )
}

@Composable
private fun OnboardingProgressIndicator(progress: Float, currentStep: Int, totalSteps: Int) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedCircularProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
        )

        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = Color.White
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${currentStep}/${totalSteps}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    val state = OnboardingState()

    ComponentPreviewTemplate {
        OnboardingScreenImpl(
            state = state.copy(currentStep = state.steps[1]),
            executeIntent = {},
        )
    }
}