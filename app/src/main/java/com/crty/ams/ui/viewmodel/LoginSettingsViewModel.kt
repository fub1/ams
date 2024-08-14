// LoginSettingsViewModel.kt
package com.crty.ams.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginSettingsViewModel @Inject constructor() : ViewModel() {
    var serverName by mutableStateOf("http://localhost")
    var serverPort by mutableIntStateOf(8080)

    fun saveSettings() {
        // TODO: Save serverName and serverPort to your application preferences
        // You can use `SharedPreferences` or another data persistence method
    }
}