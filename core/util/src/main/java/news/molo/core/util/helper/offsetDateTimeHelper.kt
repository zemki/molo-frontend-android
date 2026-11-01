package news.molo.core.util.helper

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

fun OffsetDateTime.timeSinceNow(): String {
    val now = OffsetDateTime.now()
    return if (toLocalDate() == now.toLocalDate()) {
        val timeFormatter =
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN)
        this.format(timeFormatter) + " Uhr"
    } else {
        val days = ChronoUnit.DAYS.between(toLocalDate(), now.toLocalDate()).coerceAtLeast(1)
        "${days}T."
    }
}


fun Long.millisToOffsetDateTime(): OffsetDateTime {
    val systemZone = ZoneId.systemDefault()
    return Instant.ofEpochMilli(this)
        .atZone(systemZone)
        .toOffsetDateTime()
}

fun formatDateTimeRange(start: OffsetDateTime, end: OffsetDateTime): String {

    val germanLocale = Locale.getDefault()
    val isSameDay = start.toLocalDate().isEqual(end.toLocalDate())

    if (isSameDay) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", germanLocale)
        val hourFormatter = DateTimeFormatter.ofPattern("HH")

        val datePart = start.format(dateFormatter)
        val startHour = start.format(hourFormatter)
        val endHour = end.format(hourFormatter)
        return "$datePart , $startHour-$endHour Uhr"

    } else {
        val fullFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH 'Uhr'", germanLocale)

        val startStr = start.format(fullFormatter)
        val endStr = end.format(fullFormatter)

        return "$startStr - $endStr"
    }
}