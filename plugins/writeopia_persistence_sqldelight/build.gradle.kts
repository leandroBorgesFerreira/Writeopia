plugins {
    kotlin("multiplatform")
    alias(libs.plugins.sqldelight)
}

//rootProject.extra.apply {
//    set("PUBLISH_GROUP_ID", "io.writeopia")
//    set("PUBLISH_ARTIFACT_ID", "writeopia-network")
//    set("PUBLISH_VERSION", libs.versions.writeopia.get())
//}

//apply(from = "${rootDir}/scripts/publish-module.gradle")

kotlin {
    jvm {}

//    js(IR) {
//        browser()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia_models"))
                implementation(libs.kotlinx.datetime)
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
        }
    }
}