plugins {
    alias(libs.plugins.kotlinSerialization)
    kotlin("multiplatform")
}

//rootProject.extra.apply {
//    set("PUBLISH_GROUP_ID", "io.writeopia")
//    set("PUBLISH_ARTIFACT_ID", "writeopia-serialization")
//    set("PUBLISH_VERSION", libs.versions.writeopia.get())
//}
//
//apply(from = "${rootDir}/scripts/publish-module.gradle")


kotlin {
    jvm {}

    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia_models"))

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
