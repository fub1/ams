package com.crty.ams.asset.ui.asset_allocation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AssetInfo
import com.crty.ams.core.data.network.model.AssetAllocationRequest
import com.crty.ams.core.data.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class AssetAllocationViewModel @Inject constructor(
    // Inject your repository or use case here
    private val coreRepository: CoreRepository
) : ViewModel() {

    // 使用 StateFlow 来保存和管理输入框中的值
    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location
    private val _locationId = MutableStateFlow(0)
    val locationId: StateFlow<Int> = _locationId.asStateFlow()

    private val _department = MutableStateFlow("")
    val department: StateFlow<String> = _department
    private val _departmentId = MutableStateFlow(0)
    val departmentId: StateFlow<Int> = _departmentId.asStateFlow()

    private val _user = MutableStateFlow("")
    val user: StateFlow<String> = _user
    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId.asStateFlow()

    private val _ids = MutableStateFlow<List<Int>>(emptyList())
    val ids: StateFlow<List<Int>> = _ids.asStateFlow()



    // 控制动画和文字显示的状态
    val showSuccessPopup = mutableStateOf(false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isTimeout  = MutableStateFlow(false)
    val isTimeout: StateFlow<Boolean> = _isTimeout
    private val _isFailed  = MutableStateFlow(false)
    val isFailed: StateFlow<Boolean> = _isFailed
    private val _failedMessage  = MutableStateFlow("")
    val failedMessage: StateFlow<String> = _failedMessage
    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    fun fetchData(ids: List<Int>){
        _ids.value = ids
    }

    fun updateLocationValueId(newValue: String, id: Int) {
        _location.value = newValue
        _locationId.value = id
    }

    fun updateDepartmentValueId(newValue: String, id: Int) {
        if (_departmentId.value != id){
            _user.value = ""
            _userId.value = null
        }

        _department.value = newValue
        _departmentId.value = id

    }

    fun updateUserValueId(id: Int, newValue: String, departmentId: Int, departmentName: String) {
        _user.value = newValue
        _userId.value = id

        _departmentId.value = departmentId
        _department.value = departmentName

        /* 在此根据用户id获取到所属部门信息，并修改值 */
    }

    fun submit(){
        println(_locationId.value)
        println(_departmentId.value)
        println(_userId.value)

        val a = AssetAllocationRequest(
            _ids.value,
            _departmentId.value,
            _locationId.value,
            _userId.value,
            null
        )

        viewModelScope.launch {
            try {
                _isLoading.value = true
                // 设置超时时间为5秒
                val result = withTimeoutOrNull(22000) {
//                    delay(10000)
                    coreRepository.submitAssetAllocation(a)
                }
                _isLoading.value = false

                if (result == null) {
                    // 超时逻辑处理
                    // 显示超时提示信息
                    _isTimeout.value = true
                } else {
                    // 正常处理
                    if (result.getOrNull()?.code == 0){
                        performOperation()

                        //处理跳转


                    }else if (result.getOrNull()?.code == 1){
                        _isFailed.value = true
                        _failedMessage.value = result.getOrNull()?.message.toString()
                    }else if (result.getOrNull()?.code == -1){
                        //退出登录

                    }

                }
            } catch (e: Exception) {
                // 处理其他异常
                _isLoading.value = false
                _isError.value = true

            }
        }




    }


    // 模拟执行操作的方法
    fun performOperation() {
        viewModelScope.launch {
            // 显示弹窗
            showSuccessPopup.value = true
            // 延迟2秒后隐藏弹窗
            delay(2000)
            showSuccessPopup.value = false
        }
    }

    fun dismissTimeoutDialog() {
        _isTimeout.value = false
    }
    fun dismissFieldDialog() {
        _isFailed.value = false
    }
    fun dismissErrorDialog() {
        _isError.value = false
    }
}