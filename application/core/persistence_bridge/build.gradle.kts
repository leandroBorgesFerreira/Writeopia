plugins {
    kotlin("multiplatform")
    alias(libs.plugins.androidLibrary)
}

kotlin {
    jvm {}
    androidTarget()

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":application:core:persistence_sqldelight"))
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }

        val jsMain by getting {
            dependencies {

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    publishing {
        singleVariant("release")
    }
}
