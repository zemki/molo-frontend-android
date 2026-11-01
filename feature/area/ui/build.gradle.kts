plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.area.ui"
}

dependencies {
    implementation(projects.feature.area.presentation)
}