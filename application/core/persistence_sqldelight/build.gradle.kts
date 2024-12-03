plugins {
    kotlin("multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm {}

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
                implementation(project(":plugins:writeopia_persistence_core"))
                implementation(project(":plugins:writeopia_persistence_sqldelight"))
                implementation(project(":libraries:dbtest"))
                implementation(libs.kotlinx.datetime)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.jvm)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.sqldelight.web.driver)
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.0"))
                implementation(npm("sql.js", "1.8.0"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }

    sourceSets.nativeMain.dependencies {
        implementation(libs.sqldelight.native)
    }
}

sqldelight {
    databases {
        create("WriteopiaDb") {
            packageName.set("io.writeopia.sql")
            dialect("app.cash.sqldelight:sqlite-3-30-dialect:2.0.0")
            dependency(project(":plugins:writeopia_persistence_sqldelight"))
            generateAsync.set(true)
        }
    }
}
