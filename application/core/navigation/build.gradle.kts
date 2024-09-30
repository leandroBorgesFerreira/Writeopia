plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.multiplatform.compiler)
}


kotlin {
    jvm {}

    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.compose.navigation)
                implementation(project(":application:features:note_menu"))
                implementation(project(":application:features:editor"))
                implementation(project(":application:features:account"))
                implementation(project(":application:features:global_shell"))
                implementation(project(":application:features:notifications"))
                implementation(project(":application:features:search"))
                implementation(project(":application:core:utils"))
                implementation(project(":application:core:theme"))
            }
        }
    }
}
