plugins {
    alias(libs.plugins.news.molo.presentation.feature)
}

android {
    namespace = "news.molo.feature.search.result.presentation"

}

dependencies {
    implementation(libs.forge.paging)
}