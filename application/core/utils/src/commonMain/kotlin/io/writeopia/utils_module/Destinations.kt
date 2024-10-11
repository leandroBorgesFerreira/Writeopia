package io.writeopia.utils_module

enum class Destinations(val id: String, val root: String) {
    EDITOR("note_details", "Home"),
    CHOOSE_NOTE("choose_note", "Home"),
    SEARCH("search", "Search"),
    NOTIFICATIONS("notifications", "Notifications"),
    EDIT_FOLDER("edit_folder", "Home"),
    ACCOUNT("account", "Home"),

    AUTH_REGISTER("auth_register", "Home"),

    /**
     * THis
     */
    AUTH_MENU_INNER_NAVIGATION("auth_menu_inner_navigation", "Home"),
    AUTH_MENU("auth_menu", "Home"),
    AUTH_LOGIN("auth_login", "Home")
}
