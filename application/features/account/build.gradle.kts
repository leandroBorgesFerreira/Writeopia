plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    androidTarget()
    jvm{}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":application:core:utils"))
                implementation(project(":application:core:auth_core"))

                implementation(libs.kotlinx.datetime)

                implementation(libs.material)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.appCompat)

                implementation("androidx.activity:activity-compose")
                implementation(libs.aws.amplifyframework.core.kotlin)

                implementation(libs.viewmodel.compose)
                implementation(libs.navigation.compose)
                implementation(libs.runtime.compose)

                implementation(platform("androidx.compose:compose-bom:2023.09.02"))
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

kotlin{
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9"
        }
    }
}
//
//dependencies {
//    implementation(project(":application:core:utils"))
//    implementation(project(":application:core:resources"))
//    implementation(project(":application:core:auth_core"))
//
//    implementation(libs.androidx.ktx)
//    implementation(libs.appCompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//
//    implementation(libs.aws.amplifyframework.core.kotlin)
//
//    implementation(libs.viewmodel.compose)
//    implementation(libs.runtime.compose)
//    implementation(libs.androidx.material.icons.extended)
//    implementation(libs.navigation.compose)
//
//    implementation("androidx.activity:activity-compose")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.material3:material3-window-size-class")
//
//    // Compose - Preview
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//
//    implementation(platform(libs.androidx.compose.bom))
//}