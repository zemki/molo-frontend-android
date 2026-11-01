plugins {
    alias(libs.plugins.news.molo.android.library)
}
android {
    namespace = "news.molo.core.viewmodel"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    api(projects.core.navigation)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    api(libs.forge.viewmodel)
    api(projects.core.event)
}