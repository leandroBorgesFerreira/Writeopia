import org.jetbrains.compose.compose

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("multiplatform")
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
                implementation(project(":application:core:utils"))
                implementation(project(":plugins:writeopia_network"))
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }

        val androidMain by getting {
            dependencies {
//                implementation(libs.androidx.ktx)
//                implementation(libs.appCompat)

                implementation(libs.aws.amplifyframework.cognito)
                implementation(libs.aws.amplifyframework.core.kotlin)
            }
        }
    }
}

android {
    namespace = "io.writeopia.auth.core"
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
}