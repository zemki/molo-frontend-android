package news.molo.core.util.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import java.time.OffsetDateTime

fun Context.sendEmail(to: String, subject: String, text: String) {
    val mailto = "mailto:$to" +
            "?subject=" + Uri.encode(subject) +
            "&body=" + Uri.encode(text)

    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = mailto.toUri()
    startActivity(emailIntent)
}

fun Context.shareEvent(
    eventDate: OffsetDateTime,
    title: String,
    link: String
) {
    val formattedDateTime = eventDate.format()
    ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setChooserTitle("molo Event teilen")
        .setText(
            """
    Dieses Event habe ich in der molo App gefunden:
    
    $title
    $formattedDateTime
    $link
    
    Du hast noch nicht die molo App? Hier kannst du dir die App runterladen:
    
    Google Play Store: https://play.google.com/store/apps/details?id=news.molo.android 
    Apple App Store: https://apps.apple.com/de/app/molo-news/id1486601544?l=en-GB
    """.trimIndent()
        )
        .startChooser()
}

fun Context.addEventToCalendar(
    title: String,
    description: String,
    startDate: OffsetDateTime,
    endDate: OffsetDateTime,
    location: String
) {
    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            startDate.toInstant().toEpochMilli()
        )
        .putExtra(
            CalendarContract.EXTRA_EVENT_END_TIME,
            endDate.toInstant().toEpochMilli()
        )
        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
        .putExtra(CalendarContract.Events.TITLE, title)
        .putExtra(CalendarContract.Events.DESCRIPTION, description)
        .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
    startActivity(
        Intent.createChooser(intent, "Zum Kalendar hinzufügen")
    )
}

fun Context.openLocationInMaps(
    latitude: Double,
    longitude: Double
) {
    val uri =
        "geo:${latitude},${longitude}?q=${latitude},${longitude})".toUri()
    val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
    }
}

fun Context.shareArticle(title: String, link: String) {
    val shareIntent = ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setChooserTitle("molo Artikel teilen")
        .setText(
            """
    Diesen Artikel habe ich in der molo App gefunden:
    
    $title
    $link
    
    Du hast noch nicht die molo App? Hier kannst du dir die App herunterladen:
    
    Google Play Store: https://play.google.com/store/apps/details?id=news.molo.android
    Apple App Store: https://apps.apple.com/de/app/molo-news/id1486601544?l=en-GB
    """.trimIndent()
        )
        .createChooserIntent()

    if (shareIntent.resolveActivity(packageManager) != null) {
        startActivity(shareIntent)
    }
}

