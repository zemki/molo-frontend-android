package news.molo.feature.profile.presentation

import news.molo.domain.model.Area

sealed interface ProfileNavResult {
    data class SelectedArea(val value: Area) : ProfileNavResult
}