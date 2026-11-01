plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.onboarding.ui"
}

dependencies {
    implementation(projects.feature.onboarding.presentation)
}