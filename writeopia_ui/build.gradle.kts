@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.multiplatform.compiler)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
//    alias(libs.plugins.nativeCocoapods)
}

// rootProject.extra.apply {
//    set("PUBLISH_GROUP_ID", "io.writeopia")
//    set("PUBLISH_ARTIFACT_ID", "writeopia-models")
//    set("PUBLISH_VERSION", libs.versions.writeopia.get())
// }

// apply(from = "${rootDir}/scripts/publish-module.gradle")

kotlin {
    jvm {}

    val dummy = Attribute.of("dummy", String::class.java)

    androidTarget()

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))

                implementation(libs.kotlinx.datetime)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                // Coil
                implementation(libs.coil.compose)
                implementation(libs.coil.video)
            }
        }

        val jsMain by getting {
            dependencies {
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }
    }
}

android {
    namespace = "io.writeopia.sdk.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
