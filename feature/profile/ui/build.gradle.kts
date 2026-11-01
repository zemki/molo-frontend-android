plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.profile.ui"
}

dependencies {
    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.picker)
}