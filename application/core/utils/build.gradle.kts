plugins {
    kotlin("multiplatform")
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

                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}