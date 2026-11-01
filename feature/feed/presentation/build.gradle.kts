plugins {
    alias(libs.plugins.news.molo.presentation.feature)
}

android {
    namespace = "news.molo.feature.feed.presentation"

}

dependencies {
    implementation(libs.androidx.paging.runtime.ktx)

}