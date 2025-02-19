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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "WriteopiaCoreCommonUi"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":application:core:theme"))
                implementation(project(":application:core:utils"))
                implementation(project(":application:core:models"))
                implementation(project(":writeopia_models"))
                implementation(project(":writeopia"))
                implementation(project(":writeopia_ui"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.uiToolingPreview)
            }
        }
    }
}
