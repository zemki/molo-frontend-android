package news.molo.feature.onboarding.ui.page

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import news.molo.core.designsystem.theme.MoloOrange

internal object OnboardingDefaults {
    val headline = SpanStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    )

    val headlineInOrange = SpanStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        color = Color.MoloOrange
    )

    val description = SpanStyle(
        fontSize = 16.sp,
    )

    val descriptionBold = SpanStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}