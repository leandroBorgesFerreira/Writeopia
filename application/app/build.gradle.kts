import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlinSerialization)
    id("kotlin-parcelize")
}
android {
    namespace = "io.storiesteller"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.storiesteller"
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("pixel6api31")
                    .apply {
                        // Use device profiles you typically see in Android Studio.
                        device = "Pixel 6"
                        // Use only API levels 27 and higher.
                        apiLevel = 31
                        // To include Google services, use "google".
                        systemImageSource = "aosp"
                    }
            }
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9"
        }
    }
}

dependencies {
    implementation(project(":storiesteller"))
    implementation(project(":storiesteller_persistence"))
    implementation(project(":storiesteller_serialization"))
    implementation(project(":storiesteller_network"))
    implementation(project(":storiesteller_models"))

    implementation(project(":application:utils"))
    implementation(project(":application:auth_core"))
    implementation(project(":application:resources"))
    implementation(project(":application:features:note_menu"))
    implementation(project(":application:features:editor"))
    implementation(project(":application:features:auth"))
    implementation(project(":application:features:account"))

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.aws.amplifyframework.core.kotlin)

    implementation(libs.androidx.ktx)
    implementation(libs.appCompat)
    implementation(libs.material)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization.json)
    implementation(libs.ktor.client.logging)

    implementation(libs.coil.compose)
    implementation(libs.coil.video)

    implementation(libs.viewmodel.compose)
    implementation(libs.runtime.compose)
    implementation(libs.navigation.compose)

    implementation(libs.androidx.material.icons.extended)

    implementation("androidx.activity:activity-compose")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    // Compose - Preview
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")
}
