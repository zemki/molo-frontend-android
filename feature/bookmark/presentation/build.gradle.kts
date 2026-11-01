plugins {
    alias(libs.plugins.news.molo.presentation.feature)
}

android {
    namespace = "news.molo.feature.bookmark.presentation"

}

dependencies {
    implementation(libs.forge.paging)
}