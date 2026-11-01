plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.posting.article.creation.ui"
}

dependencies {
    implementation(projects.feature.posting.article.creation.presentation)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(projects.feature.picker)
}