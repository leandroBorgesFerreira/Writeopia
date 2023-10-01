plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    alias(libs.plugins.application)
}

kotlin {
    androidTarget()

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_persistence"))
                implementation(project(":plugins:writeopia_serialization"))
                implementation(project(":plugins:writeopia_network"))

                implementation(project(":application:utils"))
                implementation(project(":application:persistence"))
                implementation(project(":application:auth_core"))
                implementation(project(":application:resources"))
                implementation(project(":application:features:note_menu"))
                implementation(project(":application:features:editor"))
                implementation(project(":application:features:auth"))
                implementation(project(":application:features:account"))

                implementation(project(":application:notes_menu_shared"))

                implementation(libs.androidx.ktx)
                implementation(libs.appCompat)

                implementation(libs.ktor.client.logging)

                implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
                implementation("androidx.navigation:navigation-compose:2.7.3")

                implementation("androidx.compose.material3:material3:1.1.2")
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


dependencies {
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.2")
}
