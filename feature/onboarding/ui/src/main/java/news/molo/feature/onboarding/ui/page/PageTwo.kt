package news.molo.feature.onboarding.ui.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import news.molo.core.designsystem.component.organism.VisualInfoContent
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
internal fun PageTwo(
    contentPaddingValues: PaddingValues,
) {
    val headline = buildAnnotatedString {
        withStyle(style = OnboardingDefaults.headlineInOrange) {
            append(stringResource(R.string.news_molo_onboarding_step2_title1))
        }
    }

    val description = buildAnnotatedString {
        withStyle(style = OnboardingDefaults.description) {
            append(stringResource(R.string.news_molo_onboarding_step2_description))
        }
    }

    VisualInfoContent(
        contentPaddingValues = contentPaddingValues,
        imagePainter = painterResource(R.drawable.img_onboarding_two),
        headline = headline,
        description = description
    )
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        PageTwo(contentPaddingValues = PaddingValues(Spacing.Medium))
    }
}