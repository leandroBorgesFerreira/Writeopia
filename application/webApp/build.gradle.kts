plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

}

kotlin {
    wasm {
        moduleName = "webApp"
        browser()
        binaries.executable()
    }

    js(IR) {
        moduleName = "webApp"
        browser {
            commonWebpackConfig {
                outputFileName = "webApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
        }

        val wasmMain by getting {
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
            }
        }
    }
}