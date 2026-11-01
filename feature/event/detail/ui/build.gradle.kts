plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.event.detail.ui"
}

dependencies {
    implementation(projects.feature.event.detail.presentation)
}