plugins {
    kotlin("multiplatform")
    alias(libs.plugins.ktlint)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm {}

    androidTarget()

    js(IR) {
        browser()
        binaries.library()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "WriteopiaFolders"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))

                implementation(project(":plugins:writeopia_serialization"))

                implementation(project(":application:core:utils"))
                implementation(project(":application:core:models"))
                implementation(project(":application:core:persistence_bridge"))
                implementation(project(":application:core:connection"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}

android {
    namespace = "io.writeopia.core.documents"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
