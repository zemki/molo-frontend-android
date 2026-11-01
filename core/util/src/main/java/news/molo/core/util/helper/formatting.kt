package news.molo.core.util.helper

import android.icu.text.DateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun OffsetDateTime.format(): String {
    val date = Date.from(this.toInstant())
    val dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.GERMAN)
    val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.GERMAN)

    val formattedDate = dateFormatter.format(date)
    val formattedTime = timeFormatter.format(date)

    return "$formattedDate, $formattedTime"
}

fun OffsetDateTime.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN)
    return formatter.format(this)
}

fun String.formatToHtml(): String = this.replace("\n", "<br>")

fun String.clearFromHtml(): String = this.replace("<br>", "\n")