plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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
                implementation(libs.compose.navigation)
            }
        }
    }
}
