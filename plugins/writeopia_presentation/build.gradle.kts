plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.multiplatform.compiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlint)
}

// rootProject.extra.apply {
//    set("PUBLISH_GROUP_ID", "io.writeopia")
//    set("PUBLISH_ARTIFACT_ID", "writeopia-serialization")
//    set("PUBLISH_VERSION", libs.versions.writeopia.get())
// }
//
// apply(from = "${rootDir}/scripts/publish-module.gradle")

kotlin {
    jvm {}

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia_models"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(libs.coil.compose)
                implementation(libs.coil.ktor3)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

tasks.dokkaHtmlPartial {
    moduleName = "plugin:writeopia_presentation"
}
