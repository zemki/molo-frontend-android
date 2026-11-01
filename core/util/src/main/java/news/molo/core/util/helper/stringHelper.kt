package news.molo.core.util.helper

fun String.ensureHttps(): String {
    val hasScheme = this.contains("://")
    val isPlausibleUrl = this.contains(".") && !this.contains(" ")

    return if (!hasScheme && isPlausibleUrl) {
        "https://$this"
    } else {
        this
    }
}