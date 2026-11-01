plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.organization.ui"
}

dependencies {
    implementation(projects.feature.organization.presentation)
    implementation(projects.feature.article)
    implementation(libs.androidx.paging.compose)
}