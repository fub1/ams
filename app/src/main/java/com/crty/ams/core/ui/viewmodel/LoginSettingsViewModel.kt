package com.crty.ams.core.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.AppParameter
import com.crty.ams.core.data.network.api.CoreApiService
import com.crty.ams.core.data.network.model.SystemStampResponse
import com.crty.ams.core.data.repository.AppParameterRepository
import com.crty.ams.core.data.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSettingsViewModel @Inject constructor(
    private val appParameterRepository: AppParameterRepository,
    private val coreRepository: CoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginSettingsUiState())
    val uiState: StateFlow<LoginSettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            appParameterRepository.appParameterFlow.collect { appParameter ->
                _uiState.value = LoginSettingsUiState(
                    serverAddress = appParameter.baseUrl,
                    serverPort = appParameter.basePort
                )
            }
        }
    }

    fun onServerAddressChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(serverAddress = newValue)
    }

    fun onServerPortChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(serverPort = newValue.toIntOrNull())
    }

    fun onSaveSettingsClick() {
        viewModelScope.launch {
            try {
                val updatedAppParameter = AppParameter.newBuilder()
                    .setBaseUrl(_uiState.value.serverAddress)
                    .setBasePort(_uiState.value.serverPort ?: 8000)
                    .build()

                appParameterRepository.updateAppParameter(updatedAppParameter)
            } catch (e: Exception) {
                Log.e("LoginSettingsViewModel", "Error saving settings", e)
                // Handle error (e.g., show Snackbar)
            }
        }
    }

    fun onCheck() {
        viewModelScope.launch {
            val result = coreRepository.getSystemStamp()
            Log.d("LoginSettingsViewModel", "onCheck: $result")
            result.onSuccess { systemStampResponse ->
                // Handle successful response
                val timestamp = systemStampResponse.timestamp
                // Update UI or perform other actions with the timestamp
            }.onFailure {exception ->
                Log.e("LoginSettingsViewModel", "Error checking system stamp", exception)
                // Handle error
                // Log the error, display an error message, etc.
            }
        }
    }


}

data class LoginSettingsUiState(
    val serverAddress: String = "",
    val serverPort: Int? = null
)