plugins {
    alias(libs.plugins.news.molo.presentation.feature)
}

android {
    namespace = "news.molo.feature.explore.presentation"

}

dependencies {
    implementation(libs.androidx.paging.runtime.ktx)
}