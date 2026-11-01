package news.molo.feature.info.presentation.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import news.molo.core.resource.R

sealed class InfoMenuItem(
    @param:StringRes val titleRes: Int,
    @param:DrawableRes val iconRes: Int
) {

    companion object {
        fun allValues(): List<InfoMenuItem> = listOf(
            MySources,
            FeedbackAndContact,
            AboutMolo,
            Privacy,
            Imprint,
            TermsOfUse,
            ColorSchema
        )
    }

    data object MySources :
        InfoMenuItem(R.string.info_entry_my_sources, R.drawable.icn_info_my_sources)

    data object FeedbackAndContact :
        InfoMenuItem(R.string.info_entry_feedback_and_contact, R.drawable.icn_info_feedback) {
        data object MailContent {
            const val TO: String = "molonews@uni-bremen.de"
            const val SUBJECT: String = "[molo.news.android] Feedback"
            const val TEXT: String =
                "Hallo molo.news-Team,\n\nich habe Feedback zur App oder Infos und Neuigkeiten.\n\nMeine Nachricht: "
        }
    }

    data object AboutMolo :
        InfoMenuItem(R.string.info_entry_about_molo, R.drawable.icn_info_about_molo)

    data object Privacy : InfoMenuItem(R.string.info_entry_privacy, R.drawable.icn_info_privacy)
    data object Imprint : InfoMenuItem(R.string.info_entry_imprint, R.drawable.icn_info_imprint)
    data object TermsOfUse :
        InfoMenuItem(R.string.info_entry_terms_of_use, R.drawable.icn_info_imprint)

    data object ColorSchema :
        InfoMenuItem(R.string.info_entry_preferences, R.drawable.icn_preferences)
}