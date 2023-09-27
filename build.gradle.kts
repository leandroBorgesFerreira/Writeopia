// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.application) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("org.jetbrains.dokka") version "1.8.10"
    id("io.github.leandroborgesferreira.dag-command") version "1.6.0" apply true
    id("com.google.gms.google-services") version "4.3.15" apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    id("org.jetbrains.compose") version "1.5.10-beta02" apply false
//    kotlin("multiplatform") version "1.9.0" apply false
}
apply(from = "${rootDir}/scripts/publish-root.gradle")

dagCommand {
    filter = "all"
    defaultBranch = "origin/main"
    outputType = "json"
    printModulesInfo = true
}
