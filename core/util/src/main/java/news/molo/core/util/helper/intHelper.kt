package news.molo.core.util.helper

import java.util.Locale

fun Int.toShortString(): String {
    return when {
        this >= 1000 -> String.format(Locale.getDefault(), "%.1fk", this / 1000.0)
        else -> this.toString()
    }
}