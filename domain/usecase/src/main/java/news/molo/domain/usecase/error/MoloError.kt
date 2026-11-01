package news.molo.domain.usecase.error

sealed class MoloError(val message: String) {
    data object FileNotFound : MoloError("Dokument konnte nicht gefunden werden.")
    data object PushTokenFetchFailed : MoloError("Push Token submit failed.")
    data object ProfileUpdateFailed : MoloError("Profil konnte nicht aktualisiert werden.")
    data object DeviceIdMissing : MoloError("Geräte-ID konnte nicht abgerufen werden.")
    data object NoAreaSelected : MoloError("Keine Umgebung ausgewählt.")
    data object AreaLoadingFailed : MoloError("Auswahl konnte nicht geladen werden.")
    data object AreaSubmitFailed : MoloError("Gebiet konnte nicht übertragen werden.")

    data object LoginFailed : MoloError("Anmeldung fehlgeschlagen.")
    data object RegistrationFailed : MoloError("Registrierung fehlgeschlagen.")
    data object PasswordResetRequestFailed : MoloError("Anfrage fehlgeschlagen.")
    data object PasswordResetFailed : MoloError("Passwort konnte nicht zurückgesetzt werden.")
    data object ArticleCreationFailed : MoloError("Artikel konnte nicht erstellt werden.")
    data object EventCreationFailed : MoloError("Veranstaltung konnte nicht erstellt werden.")
    data object ArticleUpdateFailed : MoloError("Artikel konnte nicht aktualisiert werden.")
    data object EventUpdateFailed : MoloError("Veranstaltung konnte nicht aktualisiert werden.")
    data object EventLoadingFailed : MoloError("Fehler beim Laden der Events.")

    data object UserNotLoggedIn : MoloError("Benutzer ist nicht eingeloggt.")
    data object Unauthenticated : MoloError("Nicht authentifiziert.")
    data object OrganizationLoadingFailed : MoloError("Organisation konnte nicht geladen werden.")
    data object TagSelectionUpdateFailed : MoloError("Fehler beim Aktualisieren der Tags.")
    data object TagSelectionFailed : MoloError("Fehler beim Abrufen der Tags.")

    data object BookmarkUpdateFailed : MoloError("Fehler beim Aktualisieren der Favoriten.")
    data object FlagArticleFailed : MoloError("Fehler beim Flaggen des Artikels.")

    data object FlagEventFailed : MoloError("Fehler beim Flaggen des Events.")
    data object DeleteEventFailed : MoloError("Fehler beim Löschen des Events.")
    data object DeleteArticleFailed : MoloError("Fehler beim Löschen des Artikels.")
    data object ArticleFetchFailed : MoloError("Fehler beim Laden des Artikels.")

    data object SearchEntryCreationFailed : MoloError("Fehler beim speichern der Suche.")
    data object SearchEntryDeletionFailed : MoloError("Fehler beim Löschen der Suche.")

}
