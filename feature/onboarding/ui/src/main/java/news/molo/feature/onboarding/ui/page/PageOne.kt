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
internal fun PageOne(contentPaddingValues: PaddingValues) {

    val headline = buildAnnotatedString {
        withStyle(style = OnboardingDefaults.headlineInOrange) {
            append(stringResource(R.string.news_molo_onboarding_step1_title1))
        }
        appendLine()
        withStyle(style = OnboardingDefaults.headline) {
            append(stringResource(R.string.news_molo_onboarding_step1_title2))
        }
    }

    val description = buildAnnotatedString {
        withStyle(
            style = OnboardingDefaults.descriptionBold
        ) {
            append(stringResource(R.string.news_molo_onboarding_step1_description1))
        }
    }
    VisualInfoContent(
        contentPaddingValues = contentPaddingValues,
        imagePainter = painterResource(R.drawable.img_onboarding_one),
        headline = headline,
        description = description
    )
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        PageOne(contentPaddingValues = PaddingValues(Spacing.Medium))
    }
}