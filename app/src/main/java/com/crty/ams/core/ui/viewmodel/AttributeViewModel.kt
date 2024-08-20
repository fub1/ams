package com.crty.ams.core.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.data.model.Event
import com.crty.ams.core.data.repository.AttributeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//UI state
// 1- attributes: <List<AttributeEntity>  全部属性列表， 包括id、 parentId、 name
// 2-1 firstLevelOptions: <List<AttributeEntity>  第一级属性列表， 包括id、 parentId、 name
// 2-2 firstLevelSelected: <AttributeEntity>第一级选中的属性-  用于保存提交数据
// 2-3 firstLevelString: <String>第一级EditableExposedDropdownMenu中的字符串-  用于显示
// 3-1 secondLevelOptions: <List<AttributeEntity>  第二级属性列表， 包括id、 parentId、 name
// 3-2 secondLevelSelected:<AttributeEntity>第二级选中的属性
// 3-3 secondLevelString: <String>第二级EditableExposedDropdownMenu中的字符串-  用于显示
// 4-1 thirdLevelOptions: <List<AttributeEntity>  第三级属性列表， 包括id、 parentId、 name
// 4-2 thirdLevelSelected: <AttributeEntity>第三级选中的属性
// 4-3 thirdLevelString: <String>第三级EditableExposedDropdownMenu中的字符串-  用于显示
// 5- attributeName: <String>属性名称
// 6-1 message: <Event<String>>界面消息
// 6-2 addSuccess: <Event<Boolean>>属性添加成功
// 7- showLoading: <Boolean>是否显示loading
// 8- grandAddOrgPermission: <Boolean>是否有添加权限



@HiltViewModel
class AttributeViewModel @Inject constructor(
    private val repository: AttributeRepository,
    private val initialAttributeName: String = ""
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {
        //
        _uiState.value = _uiState.value.copy(showLoading = true)
        if (initialAttributeName !in listOf("Organization", "Type", "Address")) {
            _uiState.value = _uiState.value.copy(
                message = Event("Invalid attribute name: $initialAttributeName")
            )
        } else {

            viewModelScope.launch {
                try {
                    when (initialAttributeName) {
                        "Organization" -> {
                            val attributes = repository.getAllAttributes("organization", AttributeEntity.Organization::class)
                            _uiState.value = _uiState.value.copy(
                                attributes = attributes,
                            )
                        }
                        "Type" -> {
                            val attributes = repository.getAllAttributes("type", AttributeEntity.Type::class)
                            _uiState.value = _uiState.value.copy(
                                attributes = attributes,
                            )
                        }
                        "Address" -> {
                            val attributes = repository.getAllAttributes("address", AttributeEntity.Address::class)
                            _uiState.value = _uiState.value.copy(
                                attributes = attributes,
                            )
                        }
                    }
                    // 一级数据初始化，只显示一级数据,默认显示第一个数据
                    _uiState.value = _uiState.value.copy(
                        firstLevelOptions = _uiState.value.attributes.filter { it.parentId == 0 },
                        firstLevelSelectedId = _uiState.value.firstLevelOptions.firstOrNull()?.id,
                        showLoading = false
                    )




                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        showLoading = false,
                        message = Event("Failed to fetch $initialAttributeName: ${e.message}")
                    )
                }
            }
        }
    }

    // 数据选择器选择事件-L1
    fun onFirstLevelSelected(revivedAttribute: AttributeEntity<*>) {
        _uiState.value = _uiState.value.copy(
            firstLevelString = revivedAttribute.name,
            firstLevelSelectedId = revivedAttribute.id,
            secondLevelOptions = _uiState.value.attributes.filter { it.parentId == revivedAttribute.id },
            thirdLevelOptions = emptyList(),
            )
    }

    fun onSecondLevelSelected(revivedAttribute: AttributeEntity<*>) {
        _uiState.value = _uiState.value.copy(
            secondLevelString = revivedAttribute.name,
            secondLevelSelectedId = revivedAttribute.id,
            thirdLevelOptions = _uiState.value.attributes.filter { it.parentId == revivedAttribute.id },
        )
    }

    fun onThirdLevelSelected(revivedAttribute: AttributeEntity<*>) {
        _uiState.value = _uiState.value.copy(
            thirdLevelString = revivedAttribute.name,
            thirdLevelSelectedId = revivedAttribute.id,
        )
    }

    fun addAttribute() {
        viewModelScope.launch {
            //TODO
            _uiState.value = _uiState.value.copy(showLoading = true)
            sleep(1000)
            _uiState.value = _uiState.value.copy(showLoading = true)
        }
    }

    fun submitAttribute() {
        //todo
    }



    data class UIState(
        val attributes: List<AttributeEntity<*>> = emptyList(),
        val firstLevelOptions: List<AttributeEntity<*>> = emptyList(),
        val firstLevelSelectedId: Int? = null,
        val firstLevelString: String = "",
        val secondLevelOptions: List<AttributeEntity<*>> = emptyList(),
        val secondLevelSelectedId: Int? = null,
        val secondLevelString: String = "",
        val thirdLevelOptions: List<AttributeEntity<*>> = emptyList(),
        val thirdLevelSelectedId: Int? = null,
        val thirdLevelString: String = "",
        val attributeName: String = "",
        val message: Event<String> = Event(""),
        val addSuccess: Event<Boolean> = Event(false),
        val showLoading: Boolean = false,
        val grandAddOrgPermission: Boolean = true //默认有权限,TODO:后续根据权限控制
    )
}