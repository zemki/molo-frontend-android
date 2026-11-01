plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.auth.login.ui"
}

dependencies {
    implementation(projects.feature.auth.login.presentation)
}