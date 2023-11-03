package io.writeopia.utils_module

enum class Destinations(val id: String) {
    EDITOR("note_details"),
    CHOOSE_NOTE("choose_note"),
    ACCOUNT("account"),

    AUTH_REGISTER("auth_register"),

    /**
     * THis
     */
    AUTH_MENU_INNER_NAVIGATION("auth_menu_inner_navigation"),
    AUTH_MENU("auth_menu"),
    AUTH_LOGIN("auth_login"),
}