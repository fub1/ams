package com.crty.ams.core.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel(){

    private var _topBarTitle = MutableStateFlow("资产管理")//用来切换首页顶部标题
    val topBarTitle: StateFlow<String> = _topBarTitle

    fun screenSwitch(screen: String) {
        // TODO: Handle login logic here
        // You can use the `username` and `password` values

        _topBarTitle.value = screen
    }
}