plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    api(projects.domain.model)
    api(libs.forge.network.response)
    api(libs.kotlinx.coroutines.core)
}