plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    kotlin("multiplatform")
//    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()
    jvm{}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_export"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":plugins:writeopia_serialization"))

                implementation(project(":application:resources"))
                implementation(project(":application:utils"))
                implementation(project(":application:auth_core"))
                implementation(project(":application:common_ui"))
                implementation(project(":application:persistence"))

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                implementation(libs.material)

                implementation(libs.viewmodel.compose)
                implementation(libs.runtime.compose)
                implementation(libs.navigation.compose)

                implementation(libs.androidx.material.icons.extended)

                implementation(libs.accompanist.systemuicontroller)

                implementation("androidx.compose.material3:material3")
                implementation("androidx.compose.material3:material3-window-size-class")

                implementation(platform("androidx.compose:compose-bom:2023.09.02"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.appCompat)

                implementation("androidx.activity:activity-compose")
                implementation(libs.accompanist.systemuicontroller)
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }
    }
}

android {
    namespace = "io.writeopia.editor"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
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
