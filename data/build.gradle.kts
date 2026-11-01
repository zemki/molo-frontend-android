plugins {
    alias(libs.plugins.news.molo.android.library)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "news.molo.data"

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://redaktion.molo.news/api/v4/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://redaktion.molo.news/api/v4\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.tink.android)

    implementation(libs.firebase.installations)
    implementation(projects.core.util)
    api(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    api(libs.androidx.room.ktx)
    implementation(libs.forge.network.client)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(projects.domain.repository)
    implementation(projects.domain.provider)
    implementation(libs.kotlinx.datetime)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.kotlinx.coroutines.play.services)

    api(libs.forge.datastore.api)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
