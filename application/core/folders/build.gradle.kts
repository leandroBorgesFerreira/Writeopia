plugins {
    kotlin("multiplatform")
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
//                implementation(project(":application:core:utils"))
                implementation(project(":application:core:models"))
                implementation(project(":writeopia_models"))
                implementation(project(":writeopia"))

                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
