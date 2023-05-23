plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

rootProject.extra.apply {
    set("PUBLISH_GROUP_ID", "com.github.leandroborgesferreira")
    set("PUBLISH_ARTIFACT_ID", "storyteller")
    set("PUBLISH_VERSION", "0.1.0")
}

apply(from = "${rootDir}/scripts/publish-module.gradle")

android {
    namespace = "com.github.leandroferreira.storyteller"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    publishing {
        singleVariant("release")
    }
}

kotlin{
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9"
        }
    }
}

val coilVersion = "2.3.0"

dependencies {
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    // Coil
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil-video:$coilVersion")

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
}
