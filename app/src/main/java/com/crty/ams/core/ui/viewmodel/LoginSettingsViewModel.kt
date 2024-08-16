package com.crty.ams.core.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.AppParameter
import com.crty.ams.core.data.network.api.CoreApiService
import com.crty.ams.core.data.network.model.SystemStampResponse
import com.crty.ams.core.data.repository.AppParameterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSettingsViewModel @Inject constructor(
    private val appParameterRepository: AppParameterRepository,
    private val coreApiService: CoreApiService
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
        _uiState.value = _uiState.value.copy(serverPort = newValue.toIntOrNull() ?: 8080)
    }

    fun onSaveSettingsClick() {
        viewModelScope.launch {
            try {
                val updatedAppParameter = AppParameter.newBuilder()
                    .setBaseUrl(_uiState.value.serverAddress)
                    .setBasePort(_uiState.value.serverPort)
                    .build()

                appParameterRepository.updateAppParameter(updatedAppParameter)
            } catch (e: Exception) {
                Log.e("LoginSettingsViewModel", "Error saving settings", e)
                // Handle error (e.g., show Snackbar)
            }
        }
    }

    suspend fun checkApiStatus(): Result<SystemStampResponse> {
        return try {
            val baseUrl = with(_uiState.value) {
                if (serverAddress.startsWith("http://") || serverAddress.startsWith("https://")) {
                    "$serverAddress:$serverPort"
                } else {
                    "http://$serverAddress:$serverPort"
                }
            }

            val fullUrl = "$baseUrl/api/sys/stamp"
            Log.i("LoginSettingsViewModel", "API URL: $fullUrl")
            val response = coreApiService.getSystemStamp(fullUrl)
            Log.i("LoginSettingsViewModel", "API Response: ${response.code()}")
            Log.i("LoginSettingsViewModel", "API Response Body: ${response.body()}")

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }

        } catch (e: Exception) {
            Log.e("LoginSettingsViewModel", "API Check Error", e)
            Result.failure(Exception("Network Error: ${e.message}"))
        }
    }
}

data class LoginSettingsUiState(
    val serverAddress: String = "",
    val serverPort: Int = 8080
)