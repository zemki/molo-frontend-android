import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidPresentationFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(
                versionCatalog.findPlugin("news-molo-android-library").get().get().pluginId
            )

            dependencies {
                listOf(
                    ":core:resource",
                    ":domain:usecase",
                    ":core:util",
                    ":core:event",
                    ":core:viewmodel"
                ).forEach { module ->
                    add("implementation", project(module))
                }
            }
        }
    }
}