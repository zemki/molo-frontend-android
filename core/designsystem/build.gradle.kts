plugins {
    alias(libs.plugins.news.molo.compose.library)
}

android {
    namespace = "news.molo.core.designsystem"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.maps.compose)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.compose)

    implementation(projects.core.util)
    implementation(projects.core.resource)
}