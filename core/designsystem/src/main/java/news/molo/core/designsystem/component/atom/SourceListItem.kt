package news.molo.core.designsystem.component.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.util.preview.ComponentPreview

@Composable
fun SourceListItem(
    name: String,
    imageURL: String,
    isFollowing: Boolean = true,
    onFollowingChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ImageWithPlaceholder(imageUri = imageURL, modifier = Modifier.size(48.dp))

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Switch(
                checked = isFollowing,
                onCheckedChange = onFollowingChanged,
                thumbContent = if (isFollowing) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )

            val text = when (isFollowing) {
                true -> "Folgen"
                false -> "Entfolgen"
            }

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private class Provider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
}

@ComponentPreview
@Composable
private fun Preview(
    @PreviewParameter(Provider::class) state: Boolean
) {
    ComponentPreviewTemplate {
        SourceListItem(
            name = "Buten un binnen",
            imageURL = "",
            isFollowing = state,
            onFollowingChanged = {}
        )
    }
}

