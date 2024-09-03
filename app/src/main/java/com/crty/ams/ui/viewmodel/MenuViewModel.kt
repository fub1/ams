package com.crty.ams.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {
    var selectedTab by mutableStateOf("assets")

    private var _isLoggedIn = MutableStateFlow(false)//用来监听是否登录成功
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun onLoginClick() {
        // TODO: Handle login logic here
        // You can use the `username` and `password` values

        _isLoggedIn.value = true//模拟登陆成功
    }
}