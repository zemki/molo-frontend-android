plugins {
    alias(libs.plugins.news.molo.presentation.feature)
}

android {
    namespace = "news.molo.feature.event.presentation"

}

dependencies {
    implementation(libs.androidx.paging.runtime.ktx)
}