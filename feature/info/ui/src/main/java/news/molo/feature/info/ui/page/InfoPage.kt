package news.molo.feature.info.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import news.molo.core.designsystem.component.atom.Switch
import news.molo.core.designsystem.component.molecule.AccountDeletionAlert
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.component.template.OptionCardListTemplate
import news.molo.core.designsystem.component.template.PageTemplateWithPaddingSlot
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.designsystem.theme.isDarkTheme
import news.molo.core.util.preview.ComponentPreview
import news.molo.domain.model.ColorSchemaPreference
import news.molo.domain.model.MoloLinks
import news.molo.feature.info.presentation.util.InfoMenuItem
import news.molo.feature.info.ui.component.InfoActionButtons
import news.molo.feature.info.ui.component.InfoRow

@Composable
internal fun InfoPage(
    isDarkTheme: Boolean,
    infoMenuItems: List<InfoMenuItem>,
    isLoggedIn: Boolean,
    onRequestLogin: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onSendFeedback: () -> Unit,
    onChangeTheme: (ColorSchemaPreference) -> Unit,
    onClickMySources: () -> Unit,
    onAccountDeletionRequest: () -> Unit,
    onClickLogout: () -> Unit
) {
    var openDeleteAccountDialog by remember { mutableStateOf(false) }

    if (openDeleteAccountDialog) {
        AccountDeletionAlert(
            onDismiss = { openDeleteAccountDialog = false },
            onConfirm = { openDeleteAccountDialog = false; onAccountDeletionRequest() }
        )
    }

    PageTemplateWithPaddingSlot(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            OptionCardList(
                isDarkTheme = isDarkTheme,
                items = infoMenuItems,
                onChangeTheme = onChangeTheme,
                onClickAbout = { onOpenUrl(MoloLinks.ABOUT_MOLO_URL) },
                onClickPrivacy = { onOpenUrl(MoloLinks.PRIVACY_URL) },
                onClickImprint = { onOpenUrl(MoloLinks.IMPRINT_URL) },
                onClickFeedback = onSendFeedback,
                onClickMySources = onClickMySources,
                onClickTerms = { onOpenUrl(MoloLinks.TERMS_URL) }
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

            InfoActionButtons(
                isLoggedIn = isLoggedIn,
                onRequestLogin = onRequestLogin,
                onLogout = onClickLogout,
                onDeleteAccount = { openDeleteAccountDialog = true }
            )
        }
    }
}

@Composable
private fun OptionCardList(
    isDarkTheme: Boolean,
    items: List<InfoMenuItem>,
    onChangeTheme: (ColorSchemaPreference) -> Unit,
    onClickAbout: () -> Unit,
    onClickPrivacy: () -> Unit,
    onClickImprint: () -> Unit,
    onClickFeedback: () -> Unit,
    onClickMySources: () -> Unit,
    onClickTerms: () -> Unit
) {
    OptionCardListTemplate(
        items = items
    ) { menuItem ->
        when (menuItem) {
            is InfoMenuItem.ColorSchema -> ColorSchemaInfoRow(
                isDarkTheme = isDarkTheme,
                onChangeTheme = onChangeTheme
            )

            else -> InfoRow(
                menuItem = menuItem,
                onClick = { clickedItem ->
                    when (clickedItem) {
                        InfoMenuItem.AboutMolo -> onClickAbout()
                        InfoMenuItem.ColorSchema -> {}
                        InfoMenuItem.FeedbackAndContact -> onClickFeedback()
                        InfoMenuItem.Imprint -> onClickImprint()
                        InfoMenuItem.MySources -> onClickMySources()
                        InfoMenuItem.Privacy -> onClickPrivacy()
                        InfoMenuItem.TermsOfUse -> onClickTerms()
                    }
                }
            )
        }
    }
}

@Composable
private fun ColorSchemaInfoRow(
    isDarkTheme: Boolean,
    onChangeTheme: (ColorSchemaPreference) -> Unit,
) {
    InfoRow(
        menuItem = InfoMenuItem.ColorSchema,
        trailingContent = {
            ColorSchemaSwitch(onChangeTheme = onChangeTheme)
        },
        onClick = { _ ->
            onChangeTheme(
                when (isDarkTheme) {
                    true -> ColorSchemaPreference.Light
                    false -> ColorSchemaPreference.Dark
                }
            )
        }
    )
}

@Composable
private fun ColorSchemaSwitch(
    onChangeTheme: (ColorSchemaPreference) -> Unit
) {
    Switch(
        checked = MaterialTheme.isDarkTheme,
        onCheckedChange = { isChecked ->
            onChangeTheme(
                when (isChecked) {
                    true -> ColorSchemaPreference.Dark
                    false -> ColorSchemaPreference.Light
                }
            )
        }
    )
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        InfoPage(
            onRequestLogin = {},
            infoMenuItems = InfoMenuItem.allValues(),
            isLoggedIn = true,
            onChangeTheme = {},
            onClickMySources = { },
            onAccountDeletionRequest = {},
            onClickLogout = {},
            isDarkTheme = true,
            onOpenUrl = { },
            onSendFeedback = { },
        )
    }
}
