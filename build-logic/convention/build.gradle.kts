plugins {
    `kotlin-dsl`
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidUIFeature") {
            id = libs.plugins.news.molo.ui.feature.get().pluginId
            implementationClass = "AndroidUIFeatureConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.news.molo.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("composeLibrary") {
            id = libs.plugins.news.molo.compose.library.get().pluginId
            implementationClass = "ComposeLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.news.molo.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidPresentationFeature") {
            id = libs.plugins.news.molo.presentation.feature.get().pluginId
            implementationClass = "AndroidPresentationFeatureConventionPlugin"
        }

    }
}
