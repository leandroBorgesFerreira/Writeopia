import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    id("org.jetbrains.compose")
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation(project(":writeopia_models"))
    implementation(project(":plugins:writeopia_serialization"))
    implementation(project(":plugins:writeopia_network"))
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