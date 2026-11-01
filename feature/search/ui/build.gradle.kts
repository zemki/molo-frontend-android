plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.explore.ui"
}

dependencies {
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    implementation(projects.feature.article)
}