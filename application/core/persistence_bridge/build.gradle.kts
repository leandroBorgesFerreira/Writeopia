plugins {
    kotlin("multiplatform")
    alias(libs.plugins.androidLibrary)
}

kotlin {
    jvm {}
    androidTarget()

//    js(IR) {
//        browser()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }

        val jvmMain by getting {
            dependencies {
                api(project(":plugins:writeopia_persistence_sqldelight"))
            }
        }

        val androidMain by getting {
            dependencies {
                api(project(":application:core:persistence_room"))
            }
        }
    }
}


android {
    namespace = "io.writeopia.sdk.persistence.bridge"
    compileSdk = 34

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release")
    }
}