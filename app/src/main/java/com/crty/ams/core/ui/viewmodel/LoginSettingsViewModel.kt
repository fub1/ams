// LoginSettingsViewModel.kt
package com.crty.ams.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.AppParameter
import com.crty.ams.core.data.repository.AppParameterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSettingsViewModel @Inject constructor(
    private val appParameterRepository: AppParameterRepository
) : ViewModel() {
    private val _serverName = MutableStateFlow("")
    private val _serverPort = MutableStateFlow(0)

    val uiState: StateFlow<LoginSettingsUiState> = combine(
        appParameterRepository.appParameterFlow,
        _serverName,
        _serverPort
    ) { appParameter, serverName, serverPort ->
        LoginSettingsUiState(
            serverName = serverName, // Use the updated server name
            serverPort = serverPort  // Use the updated server port
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LoginSettingsUiState()
        )

    init {
        // Load initial values from DataStore
        viewModelScope.launch {
            appParameterRepository.appParameterFlow.collectLatest { appParameter ->
                _serverName.value = appParameter.baseUrl
                _serverPort.value = appParameter.basePort
            }
        }
    }

    fun onServerNameChanged(newValue: String) {
        _serverName.value = newValue
    }

    fun onServerPortChanged(newValue: String) {
        _serverPort.value = try {
            newValue.toInt()
        } catch (e: NumberFormatException) {
            8080 // Default port
        }
    }

    suspend fun onSaveSettingsClick() {
        // Save settings to your data store here using _serverName.value and _serverPort.value
        val updatedAppParameter = AppParameter.newBuilder()
            .setBaseUrl(_serverName.value)
            .setBasePort(_serverPort.value)
            .build()
        appParameterRepository.updateAppParameter(updatedAppParameter)
    }
}

data class LoginSettingsUiState(
    val serverName: String = "",
    val serverPort: Int = 0
)