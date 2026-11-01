plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.auth.reset.request.ui"
}

dependencies {
    implementation(projects.feature.auth.reset.request.presentation)
}