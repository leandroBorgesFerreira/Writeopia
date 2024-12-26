plugins {
    kotlin("multiplatform")
//    id("kotlin-parcelize")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.multiplatform.compiler)
    alias(libs.plugins.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ktlint)
}

kotlin {
    androidTarget()

    sourceSets {
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(project(":application:core:common_ui_tests"))
                implementation(project(":application:core:theme"))

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
                implementation(project(":application:core:models"))
                implementation(project(":application:features:note_menu"))
                implementation(project(":application:features:editor"))
                implementation(project(":application:features:auth"))
                implementation(project(":application:features:account"))
                implementation(project(":application:features:search"))

                implementation(libs.androidx.ktx)
                implementation(libs.appCompat)

                implementation(libs.ktor.client.logging)

                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.navigation.compose)

                implementation(libs.androidx.material3)
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
        versionCode = 8
        versionName = "0.1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../../../upload-keystore.jks")
            storePassword = System.getenv("WR_ANDROID_SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("WR_ANDROID_SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("WR_ANDROID_SIGNING_KEY_PASSWORD")
        }
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
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
//        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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
