plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation(project(":application:core:persistence_sqldelight"))
                implementation(project(":application:common_flows:wide_screen_common"))
                implementation(project(":application:features:note_menu"))
                implementation(project(":writeopia_ui"))
                implementation(project(":plugins:writeopia_persistence_core"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}