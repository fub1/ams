package com.crty.ams.inventory.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CreateInventoryViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {


    // State for enabling/disabling text fields
    private val _isTextField1Enabled = MutableStateFlow(false)
    val isTextField1Enabled: StateFlow<Boolean> = _isTextField1Enabled

    private val _isTextField2Enabled = MutableStateFlow(false)
    val isTextField2Enabled: StateFlow<Boolean> = _isTextField2Enabled

    private val _isTextField3Enabled = MutableStateFlow(false)
    val isTextField3Enabled: StateFlow<Boolean> = _isTextField3Enabled

    // State for text field values
    private val _textField1Value = MutableStateFlow("")
    val textField1Value: StateFlow<String> = _textField1Value

    private val _textField2Value = MutableStateFlow("")
    val textField2Value: StateFlow<String> = _textField2Value

    private val _textField3Value = MutableStateFlow("")
    val textField3Value: StateFlow<String> = _textField3Value

    // State for enabling/disabling the toggle
    private val _isToggleEnabled = MutableStateFlow(false)
    val isToggleEnabled: StateFlow<Boolean> = _isToggleEnabled

    // State for left-right selection
    private val _selectedOption = MutableStateFlow("最低价值")
    val selectedOption: StateFlow<String> = _selectedOption


    // Update methods
    fun updateTextField1Enabled(isEnabled: Boolean) {
        _isTextField1Enabled.value = isEnabled
    }

    fun updateTextField2Enabled(isEnabled: Boolean) {
        _isTextField2Enabled.value = isEnabled
    }

    fun updateTextField3Enabled(isEnabled: Boolean) {
        _isTextField3Enabled.value = isEnabled
    }

    fun updateTextField1Value(value: String) {
        _textField1Value.value = value
    }

    fun updateTextField2Value(value: String) {
        _textField2Value.value = value
    }

    fun updateTextField3Value(value: String) {
        _textField3Value.value = value
    }

    fun updateToggleEnabled(isEnabled: Boolean) {
        _isToggleEnabled.value = isEnabled
    }

    fun updateSelectedOption(option: String) {
        _selectedOption.value = option
    }
}