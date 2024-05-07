plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    androidTarget()
    jvm()
    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_ui"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_export"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":plugins:writeopia_serialization"))
                implementation(project(":plugins:writeopia_network"))

//                implementation(project(":application:core:resources"))
                implementation(project(":application:core:utils"))
                implementation(project(":application:core:auth_core"))
                implementation(project(":application:core:common_ui"))
                implementation(project(":application:core:persistence_bridge"))

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.compose.navigation)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.appCompat)

//                implementation("androidx.activity:activity-compose")
                implementation(libs.accompanist.systemuicontroller)

                implementation(libs.viewmodel.compose)

                implementation(project.dependencies.platform(libs.androidx.compose.bom))
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}
