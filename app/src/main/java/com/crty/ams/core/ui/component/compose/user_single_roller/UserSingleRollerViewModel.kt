package com.crty.ams.core.ui.component.compose.user_single_roller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.UserAttributeEntity
import com.crty.ams.core.data.network.model.DepartmentResponse
import com.crty.ams.core.data.network.model.PersonResponse
import com.crty.ams.core.data.repository.CoreRepository
import com.crty.ams.core.ui.compose.picker.AttributeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class UserSingleRollerViewModel  @Inject constructor(
    private val coreRepository: CoreRepository
) : ViewModel() {

    private val _userList = MutableStateFlow<List<UserAttributeEntity>>(emptyList())
    val userList: StateFlow<List<UserAttributeEntity>> = _userList

    fun fetchAllAttributes(departmentId: Int){
        viewModelScope.launch {
            val result: Result<PersonResponse> = coreRepository.getUser(departmentId)
            // 遍历 result.getOrNull()?.data 并创建新的 AttributeEntity 对象
            val newAttributesList = mutableListOf<UserAttributeEntity>()
            result.getOrNull()?.data?.list?.forEach { attributeData ->
                // 假设 attributeData 是你想要转换为 AttributeEntity 的数据类型
                val attributeEntity = UserAttributeEntity(
                    // 在这里填充 AttributeEntity 的属性
                    id = attributeData.id,
                    name = attributeData.name,
                    departmentId = attributeData.departmentId,
                    departmentName = attributeData.departmentName
                    // 其他属性...
                )
                // 将新创建的 AttributeEntity 对象添加到列表中
                newAttributesList.add(attributeEntity)
            }

            _userList.value = newAttributesList
        }
    }
}