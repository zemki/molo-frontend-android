package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ImageWithPlaceholder
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.CornerRadius
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun OrganizerCard(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    title: String,
    location: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .then(modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.Medium)
            ) {
                ImageWithPlaceholder(
                    imageUri = imageUrl,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(CornerRadius.Medium))
                )

                Spacer(modifier = Modifier.width(Spacing.Small))

                Column(
                    modifier = Modifier
                        .height(64.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        OrganizerCard(
            imageUrl = "",
            title = "Convelop",
            location = "Bremen",
            onClick = {}
        )
    }
}