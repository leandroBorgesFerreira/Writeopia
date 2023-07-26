// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0-alpha12" apply false
    id("com.android.library") version "8.2.0-alpha12" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id("com.google.devtools.ksp") version "1.8.22-1.0.11" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("org.jetbrains.dokka") version "1.8.10"
    id("io.github.leandroborgesferreira.dag-command") version "1.6.0" apply true
}
apply(from = "${rootDir}/scripts/publish-root.gradle")

dagCommand {
    filter = "all"
    defaultBranch = "origin/develop"
    outputType = "json"
    printModulesInfo = true
}
