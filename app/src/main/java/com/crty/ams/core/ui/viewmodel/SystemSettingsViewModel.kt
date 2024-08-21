package com.crty.ams.core.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crty.ams.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


data class UserProfile(val avatarResId: Int, val nickname: String)

@HiltViewModel
class SystemSettingsViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {
    // StateFlow 用于保存用户信息
    private val _userProfile = MutableStateFlow(UserProfile(R.drawable.logo, "John Doe"))
    val userProfile: StateFlow<UserProfile> = _userProfile


    // 示例设备型号
    private val _deviceModel = MutableLiveData("Samsung Galaxy S21")
    val deviceModel: LiveData<String> get() = _deviceModel

    // 示例设服务端地址
    private val _serverIpModel = MutableLiveData("htps://wwwpop.com:43")
    val serverIpModel: LiveData<String> get() = _serverIpModel

    // 更新用户信息
    fun updateProfile(newProfile: UserProfile) {
        _userProfile.value = newProfile
    }
}