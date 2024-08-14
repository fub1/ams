package com.crty.ams.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {

    private val _username = MutableStateFlow("") // Example: Pre-filled username
    private val _password = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _isUserNameError = MutableStateFlow(false)
    private val _isPasswordError = MutableStateFlow(false)

    val uiState: StateFlow<LoginUiState> = combine(
        _username, _password, _isLoading, _isUserNameError, _isPasswordError
    ) { username, password, isLoading, isUserNameError, isPasswordError ->
        LoginUiState(
            username = username,
            password = password,
            isLoading = isLoading,
            usernameError = if (isUserNameError) "Username cannot be empty" else null,
            passwordError = if (isPasswordError) "Password cannot be empty" else null,
            loginError = null // Initially no login error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly, // Trigger initial validation
        initialValue = LoginUiState()
    )

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _isLoading.value = true
            // viewmodelScope timeout
            try {
                withTimeout(2000) {
                    delay(7000)

                    // Perform validation
                }
            } catch (e: TimeoutCancellationException) {
                _isLoading.value = false



                // Handle timeout
            }
            // Validation is already handled in the combine block.
            // You might want to add more robust validation logic here if needed.

            // Perform actual login - Example
            // val result = repository.login(_username.value, _password.value)

            // Handle the result - Example:
            // _isLoading.value = false
            // if (result.isSuccess) {
            //     // Handle successful login
            // } else {
            //     _uiState.value = _uiState.value.copy(
            //         loginError = result.exceptionOrNull()?.message ?: "Unknown login error"
            //     )
            // }
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginError: String? = null
)