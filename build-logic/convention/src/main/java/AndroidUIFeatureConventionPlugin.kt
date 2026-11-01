import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidUIFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPluginsByName(
                "news-molo-compose-library"
            )

            dependencies {
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":domain:model"))
                add("implementation", project(":core:resource"))
                add("implementation", project(":core:event"))
                add("implementation", project(":core:navigation"))
                add("implementation", project(":core:viewmodel"))
                add("implementation", project(":core:util"))

                add("implementation", platform(versionCatalog.findLibrary("androidx-compose-bom").get()))
                add("implementation", versionCatalog.findLibrary("androidx-ui").get())
                add("implementation", versionCatalog.findLibrary("androidx-ui-graphics").get())
                add("implementation", versionCatalog.findLibrary("androidx-ui-tooling").get())
                add("implementation", versionCatalog.findLibrary("androidx-ui-tooling-preview").get())
                add("implementation", versionCatalog.findLibrary("androidx-material3").get())
                add("implementation", versionCatalog.findLibrary("androidx-activity-compose").get())
                add("implementation", versionCatalog.findLibrary("androidx-lifecycle-runtime-ktx").get())

            }
        }
    }
}