package com.crty.ams.core.ui.compose.picker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.asset.ui.asset_allocation.viewmodel.AssetAllocationViewModel
import com.crty.ams.asset.ui.asset_change_batch.viewmodel.AssetChangeBatchViewModel
import com.crty.ams.asset.ui.asset_change_single.viewmodel.AssetChangeSingleViewModel
import com.crty.ams.asset.ui.asset_register.viewmodel.AssetRegisterViewModel
import com.crty.ams.asset.ui.asset_inventory_detail_filter.viewmodel.InventoryDetailFilterViewModel
import com.crty.ams.core.data.network.model.AssetCategoryResponse
import com.crty.ams.core.data.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@HiltViewModel
open class AttributeViewModel  @Inject constructor(
    private val coreRepository: CoreRepository
) : ViewModel() {

    private val eventShowLoading = MutableStateFlow(false)
    private val eventShowMessage = MutableStateFlow<Event<String>?>(null)
    private val attributeMode = MutableStateFlow(AttributeViewMode.SELECT)
    private val firstLevelAttributes = MutableStateFlow<List<AttributeEntity>>(emptyList())
    private val secondLevelAttributes = MutableStateFlow<List<AttributeEntity>>(emptyList())
    private val thirdLevelAttributes = MutableStateFlow<List<AttributeEntity>>(emptyList())
    private val selectedFirstLevelId = MutableStateFlow<Int?>(null)
    private val selectedSecondLevelId = MutableStateFlow<Int?>(null)
    private val selectedThirdLevelId = MutableStateFlow<Int?>(null)

    private val _attributeType = MutableStateFlow("") /* 调组件时传入的参数 */


    private var _allAttributes: List<AttributeEntity> = emptyList()


    private val _state = MutableStateFlow(AttributeViewState())
    open val state = _state.asStateFlow()

    init {
        viewModelScope.launch {

            combine(
                eventShowLoading,
                eventShowMessage,
                attributeMode,
                firstLevelAttributes,
                secondLevelAttributes,
                thirdLevelAttributes,
                selectedFirstLevelId,
                selectedSecondLevelId,
                selectedThirdLevelId,


            ) { arrayOfValues: Array<Any?> ->
                val showLoading = arrayOfValues[0] as Boolean
                val showMessage = arrayOfValues[1] as Event<*>?
                val mode = arrayOfValues[2] as AttributeViewMode
                val firstLevelAttributes = arrayOfValues[3] as List<AttributeEntity>?
                val secondLevelAttributes = arrayOfValues[4] as List<AttributeEntity>?
                val thirdLevelAttributes = arrayOfValues[5] as List<AttributeEntity>?
                val selectedFirstLevelId = arrayOfValues[6] as Int?
                val selectedSecondLevelId = arrayOfValues[7] as Int?
                val selectedThirdLevelId = arrayOfValues[8] as Int?

                AttributeViewState(
                    loading = showLoading,
                    message = showMessage,
                    mode = mode,
                    firstLevelAttributes = firstLevelAttributes,
                    secondLevelAttributes = secondLevelAttributes,
                    thirdLevelAttributes = thirdLevelAttributes,
                    selectedFirstLevelId = selectedFirstLevelId,
                    selectedSecondLevelId = selectedSecondLevelId,
                    selectedThirdLevelId = selectedThirdLevelId,
                    )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun fetchAllAttributes()
    {

        viewModelScope.launch {
            when(_attributeType.value){
                "资产分类" -> {
                    val result: Result<AssetCategoryResponse> = coreRepository.getAssetCategory()
//                    println("返回值${result.getOrNull()?.data?.get(0)?.id}")
                    // 遍历 result.getOrNull()?.data 并创建新的 AttributeEntity 对象
                    val newAttributesList = mutableListOf<AttributeEntity>()
                    result.getOrNull()?.data?.forEach { attributeData ->
                        // 假设 attributeData 是你想要转换为 AttributeEntity 的数据类型
                        val attributeEntity = AttributeEntity(
                            // 在这里填充 AttributeEntity 的属性
                            id = attributeData.id,
                            parentId = attributeData.parentId,
                            name = attributeData.description
                            // 其他属性...
                        )
                        // 将新创建的 AttributeEntity 对象添加到列表中
                        newAttributesList.add(attributeEntity)
                    }

                    _allAttributes = newAttributesList

                    _state.value = _state.value.copy(
                        firstLevelAttributes = _allAttributes.filter { it.parentId == 0 }
                    )
                }
            }
        }

//        _allAttributes = listOf(
//            AttributeEntity(1, 0,"First Level Option 1"),
//            AttributeEntity(2, 0,"First Level Option 2"),
//            AttributeEntity(3, 2,"First Level Option 2-1"),
//            AttributeEntity(4, 2,"First Level Option 2-2"),
//            AttributeEntity(5, 4,"First Level Option 2-2-1"),
//            AttributeEntity(6, 4,"First Level Option 2-2-2"),
//            AttributeEntity(7, 1,"First Level Option 1-1"),
//            AttributeEntity(8, 0,"First Level Option 4"),
//            AttributeEntity(9, 0,"First Level Option 5"),
//            AttributeEntity(10, 0,"First Level Option 6"),
//        )
    }

    fun toAttributeCreateMode() {
//        fetchAllAttributes()
        _state.value = _state.value.copy(
            mode = AttributeViewMode.CREATE,
            secondLevelAttributes = emptyList(),
            thirdLevelAttributes = emptyList()
        )
    }

    fun toAttributeSelectMode() {
//        fetchAllAttributes()
        _state.value = _state.value.copy(
            mode = AttributeViewMode.SELECT,
            secondLevelAttributes = emptyList(),
            thirdLevelAttributes = emptyList()
        )
    }

    fun addAttribute(attrName: String, attrCode: String) {
        //TODO
    }

    fun onFirstLevelSelected(id: Int) {

        _state.value = _state.value.copy(
            secondLevelAttributes = _allAttributes.filter { it.parentId == id },
            selectedFirstLevelId = _allAttributes.find { it.id == id }?.id,
            selectedSecondLevelId = null,
            selectedThirdLevelId = null,

        )
    }

    fun onSecondLevelSelected(id: Int) {
        selectedThirdLevelId.value = null
        _state.value = _state.value.copy(
            selectedSecondLevelId = _allAttributes.find { it.id == id }?.id,
            thirdLevelAttributes = _allAttributes.filter { it.parentId == id },
            selectedThirdLevelId = null,

        )
    }

    fun onThirdLevelSelected(id: Int) {
        _state.value = _state.value.copy(
            selectedThirdLevelId = _allAttributes.find { it.id == id }?.id
            )
    }

    fun getSelectedInfo(id: Int, list: List<AttributeEntity>): AttributeEntity? {
        for (attribute in list) {
            if (attribute.id == id) {
                return attribute
            }
        }
        return null // 如果没有找到匹配的实体类，则返回 null
    }

    fun setAttributeType(attributeType: String){
        _attributeType.value = attributeType
    }







    data class AttributeViewState(
        val loading: Boolean = false,
        val message: Event<*>? = null,
        val mode: AttributeViewMode = AttributeViewMode.SELECT,
        val firstLevelAttributes: List<AttributeEntity>? = emptyList<AttributeEntity>(),
        val secondLevelAttributes: List<AttributeEntity>? = emptyList<AttributeEntity>(),
        val thirdLevelAttributes: List<AttributeEntity>? = emptyList<AttributeEntity>(),
        val selectedFirstLevelId: Int? = null,
        val selectedSecondLevelId: Int? = null,
        val selectedThirdLevelId: Int? = null,
        )
    // 页面选择状态、创建窗体
    enum class AttributeViewMode { SELECT, CREATE }
}