pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Writeopia"
include(":writeopia")
include(":writeopia_models")
include(":plugins:writeopia_persistence")
include(":plugins:writeopia_serialization")
include(":plugins:writeopia_network")
include(":plugins:writeopia_export")
include(":plugins:writeopia_import_document")
include(":application:app")
include(":application:resources")
include(":application:auth_core")
include(":application:utils")
include(":application:common_ui")
include(":application:persistence")
include(":application:features:account")
include(":application:features:editor")
include(":application:features:note_menu")
include(":application:features:auth")
include(":backend:intronotes")
include(":application:persistence")
