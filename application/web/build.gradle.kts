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
        val jsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(project(":writeopia_models"))
                implementation(project(":writeopia"))
                implementation(project(":writeopia_ui"))

                implementation(project(":plugins:writeopia_serialization"))
                implementation(project(":plugins:writeopia_network"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":application:core:persistence_bridge"))
                implementation(project(":application:core:auth_core"))
                implementation(project(":application:core:utils"))
//                implementation(project(":application:features:editor"))
//                implementation(project(":application:features:note_menu"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}