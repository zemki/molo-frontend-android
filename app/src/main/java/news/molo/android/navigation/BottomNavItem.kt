package news.molo.android.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import news.molo.core.navigation.TopLevelScreen

data class BottomNavItem(
    @param:StringRes
    val title: Int,
    val route: TopLevelScreen,
    @param:DrawableRes
    val icon: Int
)