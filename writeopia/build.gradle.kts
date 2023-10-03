plugins {
    alias(libs.plugins.androidLibrary)
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

//rootProject.extra.apply {
//    set("PUBLISH_GROUP_ID", "io.writeopia")
//    set("PUBLISH_ARTIFACT_ID", "writeopia-core")
//    set("PUBLISH_VERSION", libs.versions.writeopia.get())
//}
//
//apply(from = "${rootDir}/scripts/publish-module.gradle")

kotlin {
    jvm {}

    androidTarget()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(project(":writeopia_models"))
//                implementation(libs.material3.desktop)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.material.icons.extended)
                // Coil
                implementation(libs.coil.compose)
                implementation(libs.coil.video)

                implementation(libs.androidx.ktx)

                implementation("androidx.compose.material3:material3")
                implementation("androidx.compose.material3:material3-window-size-class")

                implementation("androidx.compose.ui:ui-tooling-preview")
//                debugImplementation("androidx.compose.ui:ui-tooling")
                
//                testImplementation(libs.kotlinx.coroutines.test)

                implementation(platform("androidx.compose:compose-bom:2023.09.02"))
            }
        }
    }
}

android {
    namespace = "io.writeopia.sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

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
//        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    publishing {
        singleVariant("release")
    }
}
