package news.molo.feature.posting.article.creation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.PrimaryButton
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.PageTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
internal fun ArticleUpdatedPage(
    headline: String,
    text: String,
    onClickNext: () -> Unit
) {
    PageTemplate(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(51.dp)
                .padding(bottom = Spacing.Medium),
            painter = painterResource(R.drawable.icn_edit_posting),
            contentDescription = null
        )
        Text(text = headline, style = MaterialTheme.typography.headlineLarge)
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(onClick = onClickNext, text = stringResource(R.string.article_posting_completed_continue_button_text))
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        ArticleUpdatedPage(
            headline = "Entwurf gespeichert!",
            text = "Unter ‚Meine Posts‘ kannst du ihn jederzeit veröffentlichen.",
            onClickNext = {}
        )
    }
}