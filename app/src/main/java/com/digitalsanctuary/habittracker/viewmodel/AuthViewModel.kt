package com.digitalsanctuary.habittracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.digitalsanctuary.habittracker.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val userName: String = "",
    val userEmail: String = "",
    val navigateToLogin: Boolean = false,
    val navigateToHome: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        observeLoginStatus()
        observeUserInfo()
    }

    private fun observeLoginStatus() {
        repository.isLoggedInFlow.onEach { isLoggedIn ->
            _state.update { it.copy(isLoggedIn = isLoggedIn) }
        }.launchIn(viewModelScope)
    }

    private fun observeUserInfo() {
        repository.userName.onEach { name ->
            _state.update { it.copy(userName = name) }
        }.launchIn(viewModelScope)
        
        repository.userEmail.onEach { email ->
            _state.update { it.copy(userEmail = email) }
        }.launchIn(viewModelScope)
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Fields cannot be empty") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            // Simulate network delay
            kotlinx.coroutines.delay(1000)
            when (val result = repository.loginUser(email, password)) {
                is AuthRepository.LoginResult.Success -> {
                    _state.update { it.copy(isLoading = false, isLoggedIn = true, navigateToHome = true) }
                }
                is AuthRepository.LoginResult.UserNotFound -> {
                    _state.update { it.copy(isLoading = false, error = "User not found. Please register first") }
                }
                is AuthRepository.LoginResult.IncorrectPassword -> {
                    _state.update { it.copy(isLoading = false, error = "Incorrect password") }
                }
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPass: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPass.isBlank()) {
            _state.update { it.copy(error = "All fields are required") }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(error = "Invalid email format") }
            return
        }

        val passwordError = validatePassword(password)
        if (passwordError != null) {
            _state.update { it.copy(error = passwordError) }
            return
        }

        if (password != confirmPass) {
            _state.update { it.copy(error = "Passwords do not match") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            repository.registerUser(name, email, password)
            _state.update { 
                it.copy(
                    isLoading = false, 
                    successMessage = "Registration Successful",
                    navigateToLogin = true 
                ) 
            }
        }
    }

    private fun validatePassword(password: String): String? {
        if (password.length < 8) return "Password must be at least 8 characters"
        if (!password.any { it.isUpperCase() }) return "Password must contain an uppercase letter"
        if (!password.any { it.isLowerCase() }) return "Password must contain a lowercase letter"
        if (!password.any { it.isDigit() }) return "Password must contain a number"
        if (!password.any { !it.isLetterOrDigit() }) return "Password must contain a special character"
        return null
    }

    fun isOnboardingDone(): Flow<Boolean> {
        return repository.isOnboardingDone()
    }

    fun setOnboardingDone() {
        viewModelScope.launch {
            repository.setOnboardingDone(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            // Reset state immediately to prevent race conditions during navigation
            _state.update { it.copy(
                isLoggedIn = false, 
                navigateToHome = false, 
                navigateToLogin = true,
                error = null,
                successMessage = null
            ) }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
    
    fun clearNavigationFlags() {
        _state.update { it.copy(navigateToLogin = false, navigateToHome = false, successMessage = null) }
    }
}
