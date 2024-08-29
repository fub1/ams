package com.crty.ams.asset.ui.asset_change_batch.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

}