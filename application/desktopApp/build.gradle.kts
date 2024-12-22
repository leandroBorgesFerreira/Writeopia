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
                implementation(project(":application:core:models"))
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
                bundleID = "io.writeopia.notesApp"

                iconFile.set(iconsRoot.resolve("icon-mac.icns"))
                jvmArgs("-Dapple.awt.application.appearance=system")

                signing {
                    val providers = project.providers
                    sign.set(true)
                    identity.set(providers.environmentVariable("SIGN_IDENTITY"))
                }

                notarization {
                    val providers = project.providers
                    appleID.set(providers.environmentVariable("NOTARIZATION_APPLE_ID"))
                    password.set(providers.environmentVariable("NOTARIZATION_PASSWORD"))
                    teamID.set(providers.environmentVariable("NOTARIZATION_TEAM_ID"))
                }
            }
        }

        buildTypes.release {
            proguard {
                version.set("7.5.0")
                isEnabled = true
                configurationFiles.from("proguard-rules.pro")
            }
        }
    }
}
