import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.desktop.currentOs)

                implementation(compose.desktop.currentOs)

                implementation(project(":application:core:persistence_sqldelight"))
                implementation(project(":application:common_flows:wide_screen_common"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.writeopia.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Writeopia"
            packageVersion = "1.0.0"
        }
    }
}
