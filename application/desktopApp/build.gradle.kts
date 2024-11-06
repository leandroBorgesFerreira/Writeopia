import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.multiplatform.compiler)
    alias(libs.plugins.ktlint)
}

kotlin {
    jvm {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.desktop.currentOs)

                implementation(compose.desktop.currentOs)

                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":application:core:persistence_sqldelight"))
                implementation(project(":application:core:theme"))
                implementation(project(":application:common_flows:wide_screen_common"))
                implementation(project(":application:features:note_menu"))
                implementation(project(":writeopia_ui"))
                implementation(project(":plugins:writeopia_presentation"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.writeopia.desktop.MainDesktopKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Writeopia"
            packageVersion = "1.0.0"
            modules("java.sql")

            val iconsRoot = project.file("./src/jvmMain/resources/images")
            macOS {
                iconFile.set(iconsRoot.resolve("icon-mac.icns"))
                jvmArgs("-Dapple.awt.application.appearance=system")
            }
        }

        buildTypes.release {
            proguard {
                configurationFiles.from("proguard-rules.pro")
            }
        }
    }
}
