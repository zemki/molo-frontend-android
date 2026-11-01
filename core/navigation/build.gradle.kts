plugins {
    alias(libs.plugins.news.molo.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "news.molo.navigation"
}

dependencies {
    implementation(projects.domain.model)
    implementation(libs.kotlinx.serialization.json)
    api(libs.forge.compose.navigation.api)
}