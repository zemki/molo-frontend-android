plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.source.ui"
}

dependencies {
    implementation(projects.feature.source.presentation)
}