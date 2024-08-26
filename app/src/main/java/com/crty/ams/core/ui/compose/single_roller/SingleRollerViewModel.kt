package com.crty.ams.core.ui.compose.single_roller

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

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    init {
        // 模拟加载数据
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            // 这里可以是从数据库或者网络获取的数据
            _userList.value = listOf(
                User(1, "Alice", "12345"),
                User(2, "Bob", "67890"),
                User(3, "Charlie", "54321"),
                User(4, "Tom", "666666"),
                User(5, "James", "55555"),
                User(6, "Brown", "789456"),
                User(7, "LiLi", "321321")
            )
        }
    }
}