package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.resource.R
import news.molo.core.util.preview.ComponentPreview


@Composable
fun TicketButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(modifier = modifier, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.event_buy_tickets_button_text),
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                painter = painterResource(R.drawable.icn_tickets),
                contentDescription = null,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        TicketButton { }
    }
}