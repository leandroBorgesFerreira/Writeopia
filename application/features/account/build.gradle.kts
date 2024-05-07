plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    androidTarget()
    jvm{}
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":application:core:utils"))
                implementation(project(":application:core:auth_core"))

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

                implementation(libs.androidx.activity.compose)

                implementation(libs.viewmodel.compose)
                implementation(libs.navigation.compose)
                implementation(libs.runtime.compose)

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
    namespace = "io.writeopia.account"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

kotlin{
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9"
        }
    }
}
