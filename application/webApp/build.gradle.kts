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
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))

                implementation(compose.material)
                implementation(compose.material3)
            }
        }
    }
}

compose.experimental {
    web.application {}
}