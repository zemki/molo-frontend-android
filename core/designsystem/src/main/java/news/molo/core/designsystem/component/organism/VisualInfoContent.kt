package news.molo.core.designsystem.component.organism

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.sp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview

@Composable
fun VisualInfoContent(
    contentPaddingValues: PaddingValues,
    imagePainter: Painter,
    headline: AnnotatedString,
    description: AnnotatedString,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            painter = imagePainter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(contentPaddingValues),
            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            Text(text = headline, style = MaterialTheme.typography.headlineLarge)
            Text(text = description, fontSize = 16.sp)
        }
    }
}

@Composable
@ComponentPreview
private fun Preview() {
    ComponentPreviewTemplate {
        VisualInfoContent(
            contentPaddingValues = PaddingValues(Spacing.Medium),
            imagePainter = painterResource(R.drawable.img_onboarding_one),
            headline = buildAnnotatedString {
                append("Deine Stadt")
            },
            description = buildAnnotatedString {
                append("molo zeigt dir, was vor deiner Tür passiert – unabhängig, werbefrei und ohne Ablenkung.")
            }
        )
    }
}