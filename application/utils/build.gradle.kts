plugins {
    kotlin("multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm {}

//    js(IR) {
//        browser()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia_models"))
            }
        }
    }
}