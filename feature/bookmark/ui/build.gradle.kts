plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.bookmark.ui"
}

dependencies {
    implementation(libs.androidx.paging.compose)
    implementation(projects.feature.article)

    implementation(projects.feature.bookmark.presentation)
}