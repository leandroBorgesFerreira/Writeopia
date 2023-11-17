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
                implementation(project(":plugins:writeopia_persistence_core"))
            }
        }
    }
}
