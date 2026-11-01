plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.info.ui"
}

dependencies {
    implementation(projects.feature.info.presentation)
}