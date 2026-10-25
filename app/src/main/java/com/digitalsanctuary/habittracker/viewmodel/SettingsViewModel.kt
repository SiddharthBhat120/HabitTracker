package com.digitalsanctuary.habittracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.digitalsanctuary.habittracker.utils.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsState(
    val darkMode: Boolean = false,
    val notifications: Boolean = true,
    val reminderTime: String = "08:00",
    val userName: String = "Demo",
    val userEmail: String = "demo@google.com",
    val phoneNumber: String = "+1 (555) 0123-4567",
    val profileImage: String? = null,
    val isPremium: Boolean = false,
    val password: String = "password123"
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = PreferencesManager(application)
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                prefs.darkMode,
                prefs.notifications,
                prefs.reminderTime,
                prefs.userName,
                prefs.userEmail,
                prefs.phoneNumber,
                prefs.profileImage,
                prefs.isPremium,
                prefs.password
            ) { arr ->
                SettingsState(
                    darkMode = arr[0] as Boolean,
                    notifications = arr[1] as Boolean,
                    reminderTime = arr[2] as String,
                    userName = arr[3] as String,
                    userEmail = arr[4] as String,
                    phoneNumber = arr[5] as String,
                    profileImage = arr[6] as String?,
                    isPremium = arr[7] as Boolean,
                    password = arr[8] as String
                )
            }.collect { _state.value = it }
        }
    }

    fun setDarkMode(v: Boolean)      { viewModelScope.launch { prefs.setDarkMode(v) } }
    fun setNotifications(v: Boolean) { viewModelScope.launch { prefs.setNotifications(v) } }
    fun setReminderTime(v: String)   { viewModelScope.launch { prefs.setReminderTime(v) } }
    
    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            prefs.setUserName(name)
            prefs.setUserEmail(email)
            prefs.setPhoneNumber(phone)
        }
    }
    
    fun setProfileImage(uri: String?) {
        viewModelScope.launch {
            prefs.setProfileImage(uri)
        }
    }
    
    fun setPassword(newPassword: String) {
        viewModelScope.launch {
            prefs.setPassword(newPassword)
        }
    }
}
