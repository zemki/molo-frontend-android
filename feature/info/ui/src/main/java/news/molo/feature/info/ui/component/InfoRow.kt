package news.molo.feature.info.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.molecule.IconTextRow
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview
import news.molo.feature.info.presentation.util.InfoMenuItem

@Composable
internal fun InfoRow(
    menuItem: InfoMenuItem,
    onClick: (InfoMenuItem) -> Unit,
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    IconTextRow(
        modifier = Modifier
            .clickable { onClick(menuItem) }
            .padding(vertical = Spacing.Small),
        icon = {
            Icon(
                imageVector =
                    when (menuItem) {
                        InfoMenuItem.AboutMolo -> Icons.Default.Info
                        InfoMenuItem.ColorSchema -> Icons.Default.ColorLens
                        InfoMenuItem.FeedbackAndContact -> Icons.Default.ChatBubbleOutline
                        InfoMenuItem.Imprint -> Icons.Default.Policy
                        InfoMenuItem.MySources -> Icons.Default.FilterAlt
                        InfoMenuItem.Privacy -> Icons.Default.PrivacyTip
                        InfoMenuItem.TermsOfUse -> Icons.Default.Handshake
                    }, contentDescription = null
            )
        },
        text = stringResource(menuItem.titleRes),
        trailingContent = trailingContent
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        InfoRow(
            menuItem = InfoMenuItem.Imprint,
            onClick = {}
        )
    }
}