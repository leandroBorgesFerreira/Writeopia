plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.application)
    id("kotlin-parcelize")
}

kotlin {
    androidTarget()

    sourceSets {
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(project(":application:core:common_ui_tests"))

                implementation(libs.kotlin.test)
                implementation(libs.androidx.espresso.core)

                implementation(libs.androidx.compose.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":plugins:writeopia_serialization"))
                implementation(project(":plugins:writeopia_network"))

                implementation(project(":application:core:utils"))
                implementation(project(":application:core:persistence_bridge"))
                implementation(project(":application:core:auth_core"))
                implementation(project(":application:core:resources"))
                implementation(project(":application:features:note_menu"))
                implementation(project(":application:features:editor"))
                implementation(project(":application:features:auth"))
                implementation(project(":application:features:account"))

                implementation(libs.androidx.ktx)
                implementation(libs.appCompat)

                implementation(libs.ktor.client.logging)

                implementation("androidx.lifecycle:lifecycle-runtime-compose")
                implementation(libs.navigation.compose)

                implementation("androidx.compose.material3:material3")
                implementation(platform("androidx.compose:compose-bom:2023.09.02"))
            }
        }
    }
}

android {
    namespace = "io.writeopia"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.writeopia"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}
