plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.feed.ui"
}

dependencies {
    implementation(projects.feature.feed.presentation)
    implementation(projects.feature.article)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
}