pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
// Zugangsdaten für GitHub Packages (privates Repo CONVELOP-GbR/forge).
// Reihenfolge: local.properties im Projekt-Root -> Gradle-Property (~/.gradle/gradle.properties, -P) -> Umgebungsvariable.
val localProperties = java.util.Properties().apply {
    val file = File(rootDir, "local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun secret(property: String, env: String): String? =
    localProperties.getProperty(property)?.takeIf { it.isNotBlank() }
        ?: providers.gradleProperty(property).orNull?.takeIf { it.isNotBlank() }
        ?: System.getenv(env)?.takeIf { it.isNotBlank() }

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/CONVELOP-GbR/forge")
            credentials {
                username = secret("gpr.user", "GITHUB_ACTOR")
                password = secret("gpr.key", "GITHUB_TOKEN")
            }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Molo"
include(":app")
include(":core:designsystem")
include(":data")
include(":core:util")
include(":core:resource")
include(":core:event")
include(":core:navigation")

include(":feature:info:ui")
include(":feature:info:presentation")

include(":feature:article:detail:ui")
include(":feature:article:detail:presentation")

include(":feature:article")

include(":feature:area:ui")
include(":feature:area:presentation")

include(":feature:search:ui")

include(":feature:tags:presentation")
include(":feature:tags:ui")

include(":feature:source:ui")
include(":feature:source:presentation")

include(":feature:profile:ui")
include(":feature:profile:presentation")

include(":feature:organization:presentation")
include(":feature:organization:ui")

include(":feature:feed:presentation")
include(":feature:feed:ui")

include(":feature:event:detail:presentation")
include(":feature:event:detail:ui")

include(":feature:bookmark:presentation")
include(":feature:bookmark:ui")

include(":feature:auth:reset:request:presentation")
include(":feature:auth:reset:request:ui")

include(":feature:auth:reset:presentation")
include(":feature:auth:reset:ui")

include(":feature:auth:login:ui")
include(":feature:auth:login:presentation")

include(":feature:onboarding:ui")
include(":feature:onboarding:presentation")

include(":feature:event:presentation")
include(":feature:event:ui")

include(":feature:location:presentation")
include(":feature:location:ui")

include(":core:viewmodel")
include(":domain")
include(":domain:model")
include(":domain:provider")
include(":domain:repository")
include(":domain:usecase")

include(":feature:posting:article:creation:ui")
include(":feature:posting:article:creation:presentation")

include(":feature:auth:registration:ui")
include(":feature:auth:registration:presentation")

include(":feature:posting:event:creation:ui")
include(":feature:posting:event:creation:presentation")

include(":feature:picker")

include(":feature:search:result:ui")
include(":feature:search:result:presentation")
