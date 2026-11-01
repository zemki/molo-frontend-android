import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(
                versionCatalog.findPlugin("news-molo-android-library").get().get().pluginId
            )
            applyPluginsByName(
                "kotlin-compose"
            )

            dependencies {
                val composeBom = platform(versionCatalog.findLibrary("androidx-compose-bom").get())
                add("implementation", composeBom)
                add("implementation", versionCatalog.findLibrary("androidx-material3").get())
            }
        }
    }
}