plugins {
    alias(libs.plugins.news.molo.compose.library)
}

android {
    namespace = "news.molo.core.util"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(projects.core.event)

    implementation(libs.androidx.ui.tooling.preview)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.compose)


    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.browser)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.maps)
    implementation(projects.core.resource)
    implementation(libs.forge.network.response)
    implementation(libs.forge.paging)
    implementation(projects.domain.usecase)
}