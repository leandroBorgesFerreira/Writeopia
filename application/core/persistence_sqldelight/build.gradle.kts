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
                implementation(project(":plugins:writeopia_persistence_core"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.jvm)
            }
        }
    }
}

sqldelight {
    databases {
        create("WriteopiaDb") {
            packageName.set("io.writeopia.sql")
            dialect("app.cash.sqldelight:sqlite-3-30-dialect:2.0.0")
            dependency(project(":plugins:writeopia_persistence_sqldelight"))
        }
    }
}
