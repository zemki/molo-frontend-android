import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPluginsByName(
                "android-library",
            )

            extensions.configure<com.android.build.api.dsl.LibraryExtension> {
                configureAndroidLibrary(this)
            }

            dependencies {
                add(
                    "implementation",
                    versionCatalog.findLibrary("kotlinx-coroutines-android").get()
                )
                add("implementation", versionCatalog.findLibrary("kotlinx-coroutines-core").get())
            }
        }
    }

    private fun Project.configureAndroidLibrary(extension: com.android.build.api.dsl.LibraryExtension) {
        extension.apply {
            compileSdk = COMPILE_SDK

            defaultConfig {
                minSdk = MIN_SDK
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
                }
            }

            compileOptions {
                sourceCompatibility = JAVA_VERSION
                targetCompatibility = JAVA_VERSION
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(JVM_TARGET)
                }
            }
        }
    }
}