package news.molo.feature.event.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import news.molo.core.resource.R
import java.time.DayOfWeek
import java.time.OffsetDateTime

sealed class EventFilter(
    @param:StringRes
    val titleRes: Int,
    @param:DrawableRes
    val iconRes: Int,
) {
    data object Today : EventFilter(
        titleRes = R.string.event_filter_title_today,
        iconRes = R.drawable.icn_filter_today
    )

    data object Tomorrow : EventFilter(
        titleRes = R.string.event_filter_title_tomorrow,
        iconRes = R.drawable.icn_filter_tomorrow
    )

    data object Weekend : EventFilter(
        titleRes = R.string.event_filter_title_weekend,
        iconRes = R.drawable.icn_filter_weekend
    )

    data class Calendar(val from: OffsetDateTime? = null, val to: OffsetDateTime? = null) :
        EventFilter(
            titleRes = R.string.event_filter_title_custom,
            iconRes = R.drawable.icn_filter_calendar
        )

    companion object {
        fun all(): List<EventFilter> = listOf(
            Today,
            Tomorrow,
            Weekend,
            Calendar()
        )
    }

    fun getDateRange(): Pair<OffsetDateTime?, OffsetDateTime?> {
        return when (this) {
            is Calendar -> this.from to this.to
            Today -> {
                val now = OffsetDateTime.now()
                val startOfDay = now.toLocalDate().atStartOfDay(now.offset).toOffsetDateTime()
                val endOfDay = startOfDay.plusDays(1).minusNanos(1)
                startOfDay to endOfDay
            }

            Tomorrow -> {
                val now = OffsetDateTime.now()
                val startOfTomorrow =
                    now.toLocalDate().plusDays(1).atStartOfDay(now.offset).toOffsetDateTime()
                val endOfTomorrow = startOfTomorrow.plusDays(1).minusNanos(1)
                startOfTomorrow to endOfTomorrow
            }

            Weekend -> {
                val now = OffsetDateTime.now()
                val today = now.dayOfWeek
                val daysUntilSaturday =
                    (DayOfWeek.SATURDAY.value - today.value).let { if (it < 0) it + 7 else it }
                val startOfSaturday =
                    now.toLocalDate().plusDays(daysUntilSaturday.toLong()).atStartOfDay(now.offset)
                        .toOffsetDateTime()
                val endOfSunday = startOfSaturday.plusDays(2).minusNanos(1)
                startOfSaturday to endOfSunday
            }
        }
    }

}