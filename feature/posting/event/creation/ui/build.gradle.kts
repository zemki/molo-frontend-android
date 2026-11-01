plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.posting.event.creation.ui"
}

dependencies {
    implementation(projects.feature.posting.event.creation.presentation)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(projects.feature.picker)
}