plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.auth.registration.ui"
}

dependencies {
    implementation(projects.feature.auth.registration.presentation)
}