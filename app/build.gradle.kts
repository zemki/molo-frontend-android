import java.util.Properties

// Geheimnisse werden NICHT eingecheckt. Quelle: local.properties im Projekt-Root
// (siehe local.properties.example) oder Umgebungsvariablen.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun secret(property: String, env: String): String? =
    localProperties.getProperty(property)?.takeIf { it.isNotBlank() }
        ?: System.getenv(env)?.takeIf { it.isNotBlank() }

val keystoreFile = secret("molo.keystore.file", "MOLO_KEYSTORE_FILE")
    ?.let { path -> File(path).takeIf { it.isAbsolute } ?: rootProject.file(path) }
val hasReleaseKeystore = keystoreFile?.exists() == true

plugins {
    alias(libs.plugins.news.molo.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

kotlin {
    android {
        namespace = "news.molo.android"

        buildFeatures {
            buildConfig = true
        }

        defaultConfig {
            applicationId = "news.molo.android"
            minSdk = 28
            targetSdk = 36
            versionCode = 95
            versionName = "5.2.1-beta2"

            // Google-Maps-Key wird über den Manifest-Platzhalter ${mapsApiKey} eingesetzt.
            manifestPlaceholders["mapsApiKey"] = secret("molo.maps.apiKey", "MOLO_MAPS_API_KEY") ?: ""
        }

        signingConfigs {
            if (hasReleaseKeystore) {
                create("release") {
                    storeFile = keystoreFile
                    storePassword = secret("molo.keystore.storePassword", "MOLO_KEYSTORE_STORE_PASSWORD")
                    keyAlias = secret("molo.keystore.keyAlias", "MOLO_KEYSTORE_KEY_ALIAS")
                    keyPassword = secret("molo.keystore.keyPassword", "MOLO_KEYSTORE_KEY_PASSWORD")
                }
            } else {
                logger.warn("Kein Release-Keystore konfiguriert (molo.keystore.file). Release-Builds werden nicht signiert.")
            }
        }

        buildTypes {
            debug {
                versionNameSuffix = "-DEBUG"
                manifestPlaceholders["appName"] = "Molo (Debug)"
                signingConfig = signingConfigs.getByName("debug")
                isMinifyEnabled = false
                isShrinkResources = false
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
            release {
                manifestPlaceholders["appName"] = "Molo"

                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                if (hasReleaseKeystore) {
                    signingConfig = signingConfigs.getByName("release")
                }
            }
        }
    }
}



dependencies {

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)


    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.firebase.installations)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.lifecycle.service)

    implementation(projects.core.event)
    implementation(projects.core.navigation)
    implementation(projects.core.designsystem)
    implementation(projects.data)
    implementation(projects.domain.usecase)
    implementation(projects.domain.provider)

    implementation(projects.core.util)
    implementation(projects.core.resource)
    implementation(projects.core.viewmodel)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.forge.compose.navigation.nav2)
    implementation(libs.forge.usecase)
    implementation(libs.forge.network.ktor)

    implementation(projects.feature.info.presentation)
    implementation(projects.feature.info.ui)

    implementation(projects.feature.area.presentation)
    implementation(projects.feature.area.ui)

    implementation(projects.feature.article.detail.presentation)
    implementation(projects.feature.article.detail.ui)

    implementation(projects.feature.search.ui)

    implementation(projects.feature.search.result.presentation)
    implementation(projects.feature.search.result.ui)

    implementation(projects.feature.tags.presentation)
    implementation(projects.feature.tags.ui)

    implementation(projects.feature.source.presentation)
    implementation(projects.feature.source.ui)

    implementation(projects.feature.profile.presentation)
    implementation(projects.feature.profile.ui)

    implementation(projects.feature.organization.presentation)
    implementation(projects.feature.organization.ui)

    implementation(projects.feature.feed.presentation)
    implementation(projects.feature.feed.ui)

    implementation(projects.feature.bookmark.presentation)
    implementation(projects.feature.bookmark.ui)

    implementation(projects.feature.event.presentation)
    implementation(projects.feature.event.ui)

    implementation(projects.feature.event.detail.presentation)
    implementation(projects.feature.event.detail.ui)

    implementation(projects.feature.auth.login.presentation)
    implementation(projects.feature.auth.login.ui)

    implementation(projects.feature.auth.registration.presentation)
    implementation(projects.feature.auth.registration.ui)

    implementation(projects.feature.auth.reset.presentation)
    implementation(projects.feature.auth.reset.ui)

    implementation(projects.feature.auth.reset.request.presentation)
    implementation(projects.feature.auth.reset.request.ui)

    implementation(projects.feature.onboarding.presentation)
    implementation(projects.feature.onboarding.ui)

    implementation(projects.feature.location.presentation)
    implementation(projects.feature.location.ui)

    implementation(projects.feature.posting.article.creation.presentation)
    implementation(projects.feature.posting.article.creation.ui)

    implementation(projects.feature.posting.event.creation.presentation)
    implementation(projects.feature.posting.event.creation.ui)
}
