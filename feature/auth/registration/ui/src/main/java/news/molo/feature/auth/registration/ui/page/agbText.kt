package news.molo.feature.auth.registration.ui.page

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import news.molo.core.resource.R
@Composable
internal fun buildAgbText(): AnnotatedString {
    return buildAnnotatedString {
        append(text = stringResource(R.string.registration_agb_step_one))
        withLink(
            LinkAnnotation.Url(
                "https://molo.news/nutzungsbedingungen.html",
                TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
            )
        ) {
            append(text = stringResource(R.string.registration_agb_step_two))
        }
        append(text = stringResource(R.string.registration_agb_step_three))
    }
}
