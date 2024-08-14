package com.crty.ams.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun onLoginClick() {
        // TODO: Handle login logic here
        // You can use the `username` and `password` values
    }
}