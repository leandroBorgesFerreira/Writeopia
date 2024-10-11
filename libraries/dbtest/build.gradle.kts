plugins {
    kotlin("multiplatform")
    alias(libs.plugins.ktlint)
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
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlin.test)
            }
        }
    }
}
