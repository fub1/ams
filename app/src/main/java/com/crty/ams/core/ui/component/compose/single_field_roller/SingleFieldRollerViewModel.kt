package com.crty.ams.core.ui.component.compose.single_field_roller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SingleRollerViewModel  @Inject constructor(

) : ViewModel() {

    private val _userList = MutableStateFlow<List<String>>(emptyList())
    val userList: StateFlow<List<String>> = _userList

    init {
        // 模拟加载数据
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            // 这里可以是从数据库或者网络获取的数据
            _userList.value = listOf(
                "资产名称",
                "资产分类",
                "品牌",
                "型号",
                "序列号",
                "供应商",
                "采购日期",
                "价格",
            )
        }
    }
}