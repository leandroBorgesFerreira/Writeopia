plugins {
    kotlin("multiplatform")
}

kotlin {
    wasm {
        moduleName = "webApp"
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
        }

        val wasmMain by getting {
            dependencies {
            }
        }
    }
}