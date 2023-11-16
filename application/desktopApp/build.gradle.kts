import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
                implementation(project(":application:core:common_ui_tests"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(project(":writeopia_models"))
                implementation(project(":writeopia"))
                implementation(project(":plugins:writeopia_serialization"))
                implementation(project(":plugins:writeopia_network"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":application:core:persistence_bridge"))
                implementation(project(":application:core:auth_core"))
                implementation(project(":application:core:utils"))
                implementation(project(":application:features:editor"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.writeopia.notes.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Writeopia"
            packageVersion = "1.0.0"
        }
    }
}