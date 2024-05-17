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
                implementation(project(":application:core:persistence_sqldelight"))
                implementation(project(":application:core:utils"))

                implementation(compose.material3)
            }
        }
    }
}
