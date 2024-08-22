package com.crty.ams.asset.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AssetRegisterViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {
    private val _input1 = MutableStateFlow("")
    val input1: StateFlow<String> = _input1.asStateFlow()

    private val _input2 = MutableStateFlow("")
    val input2: StateFlow<String> = _input2.asStateFlow()

    private val _input3 = MutableStateFlow("")
    val input3: StateFlow<String> = _input3.asStateFlow()

    private val _input4 = MutableStateFlow("")
    val input4: StateFlow<String> = _input4.asStateFlow()

    private val _input5 = MutableStateFlow("")
    val input5: StateFlow<String> = _input5.asStateFlow()

    private val _input6 = MutableStateFlow("")
    val input6: StateFlow<String> = _input6.asStateFlow()

    private val _input7 = MutableStateFlow("")
    val input7: StateFlow<String> = _input7.asStateFlow()

    private val _input8 = MutableStateFlow("")
    val input8: StateFlow<String> = _input8.asStateFlow()

    private val _input9 = MutableStateFlow("")
    val input9: StateFlow<String> = _input9.asStateFlow()

    // Similarly define input3 to input9...

    // Combine all inputs into a single Flow
//    val combinedInputs: StateFlow<List<String>> = combine(
//        _input1, _input2 // Add other inputs here
//    ) { input1, input2 /* Add other inputs here */ ->
//        listOf(input1, input2 /* Add other inputs here */)
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onInput1Changed(value: String) {
        _input1.value = value
    }

    fun onInput2Changed(value: String) {
        _input2.value = value
    }

    fun onInput3Changed(value: String) {
        _input3.value = value
    }

    fun onInput4Changed(value: String) {
        _input4.value = value
    }

    fun onInput5Changed(value: String) {
        _input5.value = value
    }

    fun onInput6Changed(value: String) {
        _input6.value = value
    }

    fun onInput7Changed(value: String) {
        _input7.value = value
    }

    fun onInput8Changed(value: String) {
        _input8.value = value
    }

    fun onInput9Changed(value: String) {
        _input9.value = value
    }


    // Similarly define onInput3Changed to onInput9Changed...
}