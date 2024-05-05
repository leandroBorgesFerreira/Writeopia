plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.application)
    id("kotlin-parcelize")
    alias(libs.plugins.google.services)
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
                implementation(project(":writeopia_ui"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":plugins:writeopia_serialization"))
                implementation(project(":plugins:writeopia_network"))

                implementation(project(":application:core:utils"))
                implementation(project(":application:core:navigation"))
                implementation(project(":application:core:persistence_bridge"))
                implementation(project(":application:core:auth_core"))
                implementation(project(":application:core:resources"))
                implementation(project(":application:core:theme"))
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
                implementation(project.dependencies.platform(libs.androidx.compose.bom))
            }
        }
    }
}

android {
    namespace = "io.writeopia"
    compileSdk = 34

    defaultConfig {
        val baseUrl = System.getenv("WRITEOPIA_CLIENT_BASE_URL")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

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
            // Todo: Re enable the minification and fix R8 bugs
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        managedDevices {
            localDevices {
                create("pixel7api33") {
                    device = "Pixel 7"
                    apiLevel = 33
                    systemImageSource = "aosp"
                }
            }
        }
    }
}
