plugins {
    id("java-library")
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinSerialization)
}

//kotlin{
//    sourceSets.all {
//        languageSettings {
//            languageVersion = "1.9"
//        }
//    }
//}

dependencies {
    implementation(project(":storyteller_models"))

    implementation(libs.kotlinx.serialization.json)
}