plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.auth.reset.ui"
}

dependencies {
    implementation(projects.feature.auth.reset.presentation)
}