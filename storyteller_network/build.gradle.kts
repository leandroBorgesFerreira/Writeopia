@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}


rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", "com.github.leandroborgesferreira")
    set("PUBLISH_ARTIFACT_ID", "storyteller-network")
    set("PUBLISH_VERSION", libs.versions.storyteller.get())
}

apply(from = "${rootDir}/scripts/publish-module.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":storyteller_serialization"))
    implementation(project(":storyteller_models"))

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization.json)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.logging)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}