package com.crty.ams.asset.ui.asset_allocation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AssetAllocationViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {

    // 使用 StateFlow 来保存和管理输入框中的值
    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location
    private val _locationId = MutableStateFlow<Int?>(null)
    val locationId: StateFlow<Int?> = _locationId.asStateFlow()

    private val _department = MutableStateFlow("")
    val department: StateFlow<String> = _department
    private val _departmentId = MutableStateFlow<Int?>(null)
    val departmentId: StateFlow<Int?> = _departmentId.asStateFlow()

    private val _user = MutableStateFlow("")
    val user: StateFlow<String> = _user
    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    fun updateLocationValueId(newValue: String, id: Int) {
        _location.value = newValue
        _locationId.value = id
    }

    fun updateDepartmentValueId(newValue: String, id: Int) {
        _department.value = newValue
        _departmentId.value = id
    }

    fun updateUserValueId(newValue: String, id: Int) {
        _user.value = newValue
        _userId.value = id

        /* 在此根据用户id获取到所属部门信息，并修改值 */
    }
}