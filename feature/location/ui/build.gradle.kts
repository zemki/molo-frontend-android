plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.location.ui"
}

dependencies {
    implementation(projects.feature.location.presentation)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    implementation(libs.maps.compose)
    implementation(libs.android.maps.utils)

    implementation(libs.maps.utils.ktx)

}