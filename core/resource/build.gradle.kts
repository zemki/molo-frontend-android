import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.news.molo.android.library)
}

android {
    namespace = "news.molo.core.resource"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}