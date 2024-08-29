package com.crty.ams.core.ui.compose.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.asset.ui.asset_allocation.viewmodel.AssetAllocationViewModel
import com.crty.ams.asset.ui.asset_change_batch.viewmodel.AssetChangeBatchViewModel
import com.crty.ams.asset.ui.asset_change_single.viewmodel.AssetChangeSingleViewModel
import com.crty.ams.asset.ui.asset_register.viewmodel.AssetRegisterViewModel
import com.crty.ams.asset.ui.asset_inventory_detail_filter.viewmodel.InventoryDetailFilterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@HiltViewModel
open class AttributeViewModel  @Inject constructor(

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
        _allAttributes = listOf(
            AttributeEntity(1, 0,"First Level Option 1"),
            AttributeEntity(2, 0,"First Level Option 2"),
            AttributeEntity(3, 2,"First Level Option 2-1"),
            AttributeEntity(4, 2,"First Level Option 2-2"),
            AttributeEntity(5, 4,"First Level Option 2-2-1"),
            AttributeEntity(6, 4,"First Level Option 2-2-2"),
            AttributeEntity(7, 1,"First Level Option 1-1"),
            AttributeEntity(8, 0,"First Level Option 4"),
            AttributeEntity(9, 0,"First Level Option 5"),
            AttributeEntity(10, 0,"First Level Option 6"),
        )

        _state.value = _state.value.copy(
            firstLevelAttributes = _allAttributes.filter { it.parentId == 0 }
        )
    }

    fun toAttributeCreateMode() {
        fetchAllAttributes()
        _state.value = _state.value.copy(
            mode = AttributeViewMode.CREATE,
            secondLevelAttributes = emptyList(),
            thirdLevelAttributes = emptyList()
        )
    }

    fun toAttributeSelectMode() {
        fetchAllAttributes()
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
    fun onAttributeSelected(id: Int, list: List<AttributeEntity>, attributeType: String, assetRegisterViewModel: AssetRegisterViewModel, inventoryDetailFilterViewModel: InventoryDetailFilterViewModel, assetChangeSingleViewModel: AssetChangeSingleViewModel, assetChangeBatchViewModel: AssetChangeBatchViewModel, assetAllocationViewModel: AssetAllocationViewModel) {
        val selectedInfo = getSelectedInfo(id, list)
        selectedInfo?.let { attribute ->
            // 更新页面 ViewModel 中的输入框值和对应的 ID

            when (attributeType) {
                "资产分类" -> assetRegisterViewModel.updateAssetCategoryId(attribute.name, attribute.id)
                "部门" -> inventoryDetailFilterViewModel.updateTextField1ValueId(attribute.name, attribute.id)
                "位置" -> inventoryDetailFilterViewModel.updateTextField2ValueId(attribute.name, attribute.id)
//                "资产分类变更" -> assetChangeSingleViewModel.updateAssetCategoryId(attribute.name, attribute.id)
                "批量修改资产分类" -> assetChangeBatchViewModel.updateAttributeValueId(attribute.name, attribute.id)
                "资产调拨位置" -> assetAllocationViewModel.updateLocationValueId(attribute.name, attribute.id)
                "资产调拨部门" -> assetAllocationViewModel.updateDepartmentValueId(attribute.name, attribute.id)
                "资产调拨使用人" -> assetAllocationViewModel.updateUserValueId(attribute.name, attribute.id)
                else -> println("Invalid day")
            }
        }
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