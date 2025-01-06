@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ktlint)
    id("com.google.devtools.ksp")
}

kotlin {
    jvm {}

    androidTarget()

    js(IR) {
        browser()
        binaries.library()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "WriteopiaFeaturesSqldelight"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia"))
                implementation(project(":writeopia_models"))
                implementation(project(":plugins:writeopia_persistence_room"))
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":application:core:theme"))

                implementation(libs.room.runtime)
                implementation(libs.room.ktx)
                implementation(libs.room.paging)

                implementation(libs.androidx.ktx)
                implementation(libs.appCompat)
                implementation(libs.material)

                implementation(libs.kotlinx.datetime)
            }
        }

        val jvmMain by getting {
            dependencies {

            }
        }
    }
}

android {
    namespace = "io.writeopia.persistence"
    compileSdk = 35

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

dependencies {
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.test)
    debugImplementation(libs.androidx.compose.test.manifest)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(project(":libraries:dbtest"))

    testImplementation(libs.kotlin.test)
}
