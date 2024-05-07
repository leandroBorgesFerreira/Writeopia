@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
//    alias(libs.plugins.nativeCocoapods)
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
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {}
        }

        val jsMain by getting {
            dependencies {
            }
        }
    }
}
