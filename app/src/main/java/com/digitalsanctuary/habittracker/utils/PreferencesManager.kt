package com.digitalsanctuary.habittracker.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    companion object {
        val IS_LOGGED_IN    = booleanPreferencesKey("is_logged_in")
        val USER_NAME       = stringPreferencesKey("user_name")
        val USER_EMAIL      = stringPreferencesKey("user_email")
        val PHONE_NUMBER    = stringPreferencesKey("phone_number")
        val PROFILE_IMAGE   = stringPreferencesKey("profile_image_uri")
        val PASSWORD        = stringPreferencesKey("user_password")
        val DARK_MODE       = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS   = booleanPreferencesKey("notifications")
        val REMINDER_TIME   = stringPreferencesKey("reminder_time")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val IS_PREMIUM      = booleanPreferencesKey("is_premium")
    }

    val isLoggedIn: Flow<Boolean>     = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[IS_LOGGED_IN] ?: false }
    val userName: Flow<String>        = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[USER_NAME] ?: "Demo" }
    val userEmail: Flow<String>       = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[USER_EMAIL] ?: "demo@google.com" }
    val phoneNumber: Flow<String>     = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[PHONE_NUMBER] ?: "+1 (555) 0123-4567" }
    val profileImage: Flow<String?>    = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[PROFILE_IMAGE] }
    val password: Flow<String>        = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[PASSWORD] ?: "password123" }
    val darkMode: Flow<Boolean>       = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[DARK_MODE] ?: false }
    val notifications: Flow<Boolean>  = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[NOTIFICATIONS] ?: true }
    val reminderTime: Flow<String>    = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[REMINDER_TIME] ?: "08:00" }
    val onboardingDone: Flow<Boolean> = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[ONBOARDING_DONE] ?: false }
    val isPremium: Flow<Boolean>      = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it[IS_PREMIUM] ?: false }

    suspend fun setLoggedIn(v: Boolean)      { context.dataStore.edit { it[IS_LOGGED_IN] = v } }
    suspend fun setUserName(v: String)       { context.dataStore.edit { it[USER_NAME] = v } }
    suspend fun setUserEmail(v: String)      { context.dataStore.edit { it[USER_EMAIL] = v } }
    suspend fun setPhoneNumber(v: String)    { context.dataStore.edit { it[PHONE_NUMBER] = v } }
    suspend fun setProfileImage(v: String?)   { context.dataStore.edit { if (v == null) it.remove(PROFILE_IMAGE) else it[PROFILE_IMAGE] = v } }
    suspend fun setPassword(v: String)       { context.dataStore.edit { it[PASSWORD] = v } }
    suspend fun setDarkMode(v: Boolean)      { context.dataStore.edit { it[DARK_MODE] = v } }
    suspend fun setNotifications(v: Boolean) { context.dataStore.edit { it[NOTIFICATIONS] = v } }
    suspend fun setReminderTime(v: String)   { context.dataStore.edit { it[REMINDER_TIME] = v } }
    suspend fun setOnboardingDone(v: Boolean){ context.dataStore.edit { it[ONBOARDING_DONE] = v } }
    suspend fun setPremium(v: Boolean)       { context.dataStore.edit { it[IS_PREMIUM] = v } }
    suspend fun logout()                     { context.dataStore.edit { it[IS_LOGGED_IN] = false } }
}
