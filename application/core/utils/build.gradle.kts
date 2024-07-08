plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.multiplatform.compiler)
}

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

                implementation(compose.foundation)
                implementation(compose.material3)

                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
