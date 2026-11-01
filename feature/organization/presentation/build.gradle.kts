plugins {
    alias(libs.plugins.news.molo.presentation.feature)
}

android {
    namespace = "news.molo.feature.organization.presentation"
}

dependencies {
    implementation(libs.forge.paging)
}