plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    api(projects.domain.repository)
    api(projects.domain.provider)
    api(libs.forge.usecase)
}