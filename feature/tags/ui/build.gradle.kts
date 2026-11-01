plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.tags.ui"
}

dependencies {
    implementation(projects.feature.tags.presentation)
}