package com.crty.ams.core.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.data.model.Event
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
        selectedSecondLevelId.value = null
        selectedThirdLevelId.value = null
        _state.value = _state.value.copy(
            secondLevelAttributes = _allAttributes.filter { it.parentId == id }

        )
    }

    fun onSecondLevelSelected(id: Int) {
        selectedThirdLevelId.value = null
        _state.value = _state.value.copy(
            thirdLevelAttributes = _allAttributes.filter { it.parentId == id }

        )
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