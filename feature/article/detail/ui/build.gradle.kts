plugins {
    alias(libs.plugins.news.molo.ui.feature)
}

android {
    namespace = "news.molo.feature.article.detail.ui"
}

dependencies {
    implementation(projects.feature.article.detail.presentation)
}