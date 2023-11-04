package io.writeopia.auth.core.repository

import android.content.SharedPreferences
import io.writeopia.auth.core.utils.USER_OFFLINE

class SharedPrefsAuthRepository(private val sharedPreferences: SharedPreferences): AuthRepository {

    override fun saveUserChoiceOffline() {
        sharedPreferences.edit().putBoolean(USER_OFFLINE, true).apply()
    }

    override fun eraseUserChoiceOffline() {
        sharedPreferences.edit().putBoolean(USER_OFFLINE, false).commit()
    }

    override fun isUserOfflineByChoice(): Boolean = sharedPreferences.getBoolean(USER_OFFLINE, false)
}