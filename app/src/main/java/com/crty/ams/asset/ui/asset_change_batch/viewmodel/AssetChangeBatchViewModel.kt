package com.crty.ams.asset.ui.asset_change_batch.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AttributeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetChangeBatchViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {

    // 使用 StateFlow 来保存和管理输入框中的值
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _attributeValue = MutableStateFlow("")
    val attributeValue: StateFlow<String> = _attributeValue
    private val _attributeValueId = MutableStateFlow<Int?>(null)
    val attributeValueId: StateFlow<Int?> = _attributeValueId.asStateFlow()

    private val _type = MutableStateFlow("")
    private val _ids = MutableStateFlow<List<Int>>(emptyList())

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

    // 更新输入框值的方法
    fun updateInputText(newValue: String) {
        _inputText.value = newValue
        _attributeValue.value = ""
    }

    fun updateAttributeValue(newValue: String) {
        _attributeValue.value = newValue
    }
    fun updateAttributeValueId(newValue: String, id: Int) {
        _attributeValue.value = newValue
        _attributeValueId.value = id
    }

    fun fetchData(type: String, ids: List<Int>){
        _type.value = type
        _ids.value = ids
    }

    fun submit(){
        println(_inputText.value)
        println(_attributeValue.value)
        println(_type.value)
        println(_ids.value)

        when(_inputText.value){
            "资产名称" -> {}
            "资产分类" -> {}
            "品牌" -> {}
            "型号" -> {}
            "序列号" -> {}
            "供应商" -> {}
            "采购日期" -> {}
            "价格" -> {}
            "备注" -> {}
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