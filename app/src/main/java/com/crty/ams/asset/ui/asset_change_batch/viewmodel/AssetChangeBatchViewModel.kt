package com.crty.ams.asset.ui.asset_change_batch.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.data.network.model.AssetChangeRequest
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
class AssetChangeBatchViewModel @Inject constructor(
    // Inject your repository or use case here
    private val coreRepository: CoreRepository
) : ViewModel() {

    // 使用 StateFlow 来保存和管理输入框中的值
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _attributeValue = MutableStateFlow("")
    val attributeValue: StateFlow<String> = _attributeValue

    private val _attributeValueError = MutableStateFlow(false)
    val attributeValueError: StateFlow<Boolean> = _attributeValueError
    private val _attributeValueDialog = MutableStateFlow(false)
    val attributeValueDialog: StateFlow<Boolean> = _attributeValueDialog
    private val _attributeValueDialogMsg = MutableStateFlow("")
    val attributeValueDialogMsg: StateFlow<String> = _attributeValueDialogMsg

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
    fun updateAttributeValueError(){
        _attributeValueError.value = false
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
        var pass = true
        var req = AssetChangeRequest(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
        )

        when(_inputText.value){
            "资产名称" -> {
                if (_attributeValue.value.isEmpty()){
                    _attributeValueError.value = true
                    _attributeValueDialog.value = true
                    _attributeValueDialogMsg.value = "资产名称不能为空"
                    pass = false
                }else{
                    req = AssetChangeRequest(
                        null,
                        null,
                        null,
                        name = _attributeValue.value,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )
                }
            }
            "资产分类" -> {
                if (_attributeValue.value.isEmpty()){
                    _attributeValueError.value = true
                    _attributeValueDialog.value = true
                    _attributeValueDialogMsg.value = "资产分类不能为空"
                    pass = false
                }else{
                    req = AssetChangeRequest(
                        null,
                        null,
                        categoryId = _attributeValueId.value,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )
                }
            }
            "品牌" -> {
                if (_attributeValue.value.isEmpty()){
                    _attributeValueError.value = true
                    _attributeValueDialog.value = true
                    _attributeValueDialogMsg.value = "品牌不能为空"
                    pass = false
                }else{
                    req = AssetChangeRequest(
                        null,
                        null,
                        null,
                        null,
                        brand = _attributeValue.value,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )
                }
            }
            "型号" -> {
                if (_attributeValue.value.isEmpty()){
                    _attributeValueError.value = true
                    _attributeValueDialog.value = true
                    _attributeValueDialogMsg.value = "型号不能为空"
                    pass = false
                }else{
                    req = AssetChangeRequest(
                        null,
                        null,
                        null,
                        null,
                        null,
                        model = _attributeValue.value,
                        null,
                        null,
                        null,
                        null,
                        null,
                    )
                }
            }
            "序列号" -> {
                req = AssetChangeRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    sn = _attributeValue.value,
                    null,
                    null,
                )
            }
            "供应商" -> {
                req = AssetChangeRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    supplier = _attributeValue.value,
                    null,
                )
            }
            "采购日期" -> {
                req = AssetChangeRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    date = _attributeValue.value,
                    null,
                    null,
                    null,
                )
            }
            "价格" -> {
                if (_attributeValue.value.isEmpty()){
                    _attributeValueError.value = true
                    _attributeValueDialog.value = true
                    _attributeValueDialogMsg.value = "价格不能为空"
                    pass = false
                }else{
                    if (!isNumeric(_attributeValue.value)){
                        _attributeValueError.value = true
                        _attributeValueDialog.value = true
                        _attributeValueDialogMsg.value = "请输入正确的价格"
                        pass = false
                    }else{
                        req = AssetChangeRequest(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            price = _attributeValue.value.toDouble(),
                            null,
                            null,
                            null,
                            null,
                        )
                    }
                }
            }
            "备注" -> {
                req = AssetChangeRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    remark = _attributeValue.value,
                )
            }
        }

        if (pass){
            //调接口
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    // 设置超时时间为5秒
                    val result = withTimeoutOrNull(22000) {
//                    delay(10000)
                        if (_type.value == "批量"){
                            req.ids = _ids.value
                            coreRepository.submitAssetChange(req)
                        }else{
                            req.parentId = _ids.value[0]
                            coreRepository.submitAssetChangeGroup(req)
                        }
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
    fun dismissAttributeDialog() {
        _attributeValueDialog.value = false
    }
}


fun isNumeric(value: String): Boolean {
    return value.toDoubleOrNull() != null
}