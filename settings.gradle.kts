//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "Writeopia"

include(":writeopia")
include(":writeopia_models")
include(":plugins:writeopia_persistence_room")
include(":plugins:writeopia_serialization")
include(":plugins:writeopia_network")
include(":plugins:writeopia_export")
include(":plugins:writeopia_import_document")
include(":plugins:writeopia_persistence_core")
include(":plugins:writeopia_persistence_sqldelight")
include(":backend:intronotes")
include(":application:core:resources")
include(":application:core:auth_core")
include(":application:core:utils")
include(":application:core:common_ui")
include(":application:core:common_ui_tests")
include(":application:core:persistence_room")
include(":application:core:persistence_sqldelight")
include(":application:core:persistence_bridge")
include(":application:features:account")
include(":application:features:editor")
include(":application:features:note_menu")
include(":application:features:auth")
include(":application:desktopApp")
include(":application:web")
include(":application:androidApp")

