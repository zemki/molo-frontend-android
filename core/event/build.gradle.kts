plugins {
    alias(libs.plugins.news.molo.compose.library)
}
android {
    namespace = "news.molo.core.architecture"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.compose)
}