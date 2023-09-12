plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerialization)
}

rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", "io.storiesteller")
    set("PUBLISH_ARTIFACT_ID", "storiesteller-serialization")
    set("PUBLISH_VERSION", libs.versions.storiesteller.get())
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

    implementation(project(":storiesteller_models"))
    implementation(libs.kotlinx.serialization.json)
}