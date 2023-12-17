plugins {
    kotlin("multiplatform")
    alias(libs.plugins.nativeCocoapods)
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
//            baseName = "shared"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Common"
        homepage = "https://github.com/leandroBorgesFerreira/Writeopia"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../application/iosApp/Podfile")
        framework {
            baseName = "common"
            isStatic = true
        }
    }

    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":writeopia_models"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val jsMain by getting {
            dependencies {
            }
        }
    }
}
