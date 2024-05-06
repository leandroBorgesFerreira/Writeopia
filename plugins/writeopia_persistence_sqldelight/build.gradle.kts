plugins {
    kotlin("multiplatform")
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.dokka)
}

//rootProject.extra.apply {
//    set("PUBLISH_GROUP_ID", "io.writeopia")
//    set("PUBLISH_ARTIFACT_ID", "writeopia-network")
//    set("PUBLISH_VERSION", libs.versions.writeopia.get())
//}

//apply(from = "${rootDir}/scripts/publish-module.gradle")

kotlin {
    jvm {}

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_persistence_core"))
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
            packageName.set("io.writeopia.sdk.sql")
            dialect("app.cash.sqldelight:sqlite-3-30-dialect:2.0.0")
            generateAsync.set(true)
        }
    }
}

tasks.dokkaHtmlPartial {
    moduleName = "plugin:writeopia_persistence_sqldelight"
}
