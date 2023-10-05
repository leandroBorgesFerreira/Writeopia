plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerialization)
}

rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", "io.writeopia")
    set("PUBLISH_ARTIFACT_ID", "writeopia-serialization")
    set("PUBLISH_VERSION", libs.versions.writeopia.get())
}

apply(from = "${rootDir}/scripts/publish-module.gradle")

kotlin{
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9"
        }
    }
}

dependencies {
    implementation(project(":writeopia_models"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}