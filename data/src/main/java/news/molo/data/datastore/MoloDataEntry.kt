package news.molo.data.datastore

import de.convelop.forge.datastore.DataEntry
import news.molo.domain.model.AreaWithRadius
import news.molo.domain.model.ColorSchemaPreference

object MoloDataEntry {
    val UserID = DataEntry.int("self_user_id", 0)
    val DeviceID = DataEntry.string("device_id", "")
    val AuthToken = DataEntry.string("auth_token", "")
    val SelectedAreaName = DataEntry.string("selected_location_name", "")
    val ColorSchema = DataEntry.serializable(
        "color_schema_preference",
        ColorSchemaPreference.System,
        ColorSchemaPreference.serializer(),
        dataStoreJson
    )

    val PushToken = DataEntry.string("push_token", "")

    val SelectedAreaWithRadius = DataEntry.serializable(
        key = "area_with_radius",
        defaultValue = AreaWithRadius(),
        serializer = AreaWithRadius.serializer(),
        json = dataStoreJson
    )

    val OnboardingCompleted = DataEntry.boolean("onboarding_completed", false)
    val ProfileRefreshTrigger = DataEntry.int("profile_refresh_trigger", 1)
    val SelfUserOrganisationId = DataEntry.int("self_user_organisation_id", 0)
    val PostingRefreshTrigger = DataEntry.int("posting_refresh_trigger", 1)

}