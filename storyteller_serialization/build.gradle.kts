plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerialization)
}

rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", "com.github.leandroborgesferreira")
    set("PUBLISH_ARTIFACT_ID", "storyteller-serialization")
    set("PUBLISH_VERSION", libs.versions.storyteller.get())
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
    implementation(project(":storyteller_models"))

    implementation(libs.kotlinx.serialization.json)
}