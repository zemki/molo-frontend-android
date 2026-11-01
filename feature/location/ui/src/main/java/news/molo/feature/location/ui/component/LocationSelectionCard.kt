package news.molo.feature.location.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.theme.MoloOrange
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.resource.R
import news.molo.domain.model.SelectedLocation


@Composable
internal fun LocationSelectionCard(
    locationSelection: SelectedLocation,
    onClick: () -> Unit
) {
    val street = locationSelection.street.takeIf { it.isNotEmpty() }

    val locality = listOfNotNull(
        locationSelection.zip.takeIf { it.isNotEmpty() },
        locationSelection.town.takeIf { it.isNotEmpty() }
    ).joinToString(" ")

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),

        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = Color.MoloOrange,
                modifier = Modifier.size(40.dp)
            )

            Spacer(Modifier.width(Spacing.Medium))

            SelectedLocationColumn(street, locality)
        }
    }
}

@Composable
private fun RowScope.SelectedLocationColumn(street: String?, locality: String) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = stringResource(R.string.location_selection_card_title),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (street != null) {
            Text(
                text = street,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (locality.isNotEmpty()) {
            Text(
                text = locality,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}