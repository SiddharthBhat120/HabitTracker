package com.digitalsanctuary.habittracker.data.repository

import android.content.Context
import com.digitalsanctuary.habittracker.utils.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AuthRepository(private val context: Context) {
    private val preferencesManager = PreferencesManager(context)

    val userName: Flow<String> = preferencesManager.userName
    val userEmail: Flow<String> = preferencesManager.userEmail
    val isLoggedInFlow: Flow<Boolean> = preferencesManager.isLoggedIn

    suspend fun registerUser(name: String, email: String, password: String) {
        preferencesManager.setUserName(name)
        preferencesManager.setUserEmail(email)
        preferencesManager.setPassword(password)
    }

    suspend fun loginUser(email: String, password: String): LoginResult {
        val savedEmail = preferencesManager.userEmail.first()
        val savedPassword = preferencesManager.password.first()

        return when {
            savedEmail == "demo@google.com" && email != savedEmail -> LoginResult.UserNotFound
            savedEmail != email -> LoginResult.UserNotFound
            savedPassword != password -> LoginResult.IncorrectPassword
            else -> {
                preferencesManager.setLoggedIn(true)
                LoginResult.Success
            }
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        preferencesManager.setLoggedIn(isLoggedIn)
    }

    suspend fun isLoggedIn(): Boolean {
        return preferencesManager.isLoggedIn.first()
    }

    fun isOnboardingDone(): Flow<Boolean> {
        return preferencesManager.onboardingDone
    }

    suspend fun setOnboardingDone(isDone: Boolean) {
        preferencesManager.setOnboardingDone(isDone)
    }

    suspend fun logout() {
        preferencesManager.logout()
    }

    sealed class LoginResult {
        object Success : LoginResult()
        object UserNotFound : LoginResult()
        object IncorrectPassword : LoginResult()
    }
}
