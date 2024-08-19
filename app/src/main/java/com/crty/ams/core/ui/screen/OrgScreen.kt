package com.crty.ams.core.ui.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.crty.ams.core.data.model.Event
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.ui.component.LoadingContainer
import com.crty.ams.core.ui.component.EditableExposedDropdownMenu

@Composable
fun AttributeSheet(
    attributeName: String,
    firstLevelOptions:List<AttributeEntity>,
    secondLevelOptions:List<AttributeEntity>,
    thirdLevelOptions:List<AttributeEntity>,
    firstLevelSelected: AttributeEntity,
    secondLevelSelected: AttributeEntity,
    thirdLevelSelected: AttributeEntity,
    showLoading: Boolean,
    grandAddOrgPermission: Boolean,
    showMessage: Event<String>?,
    goBack: () -> Unit = {},
    add: (
        firstLevelAttributeToCreate: String,
        secondLevelAttributeCreate: String?,
        thirdLevelAttributeCreate: String?,
    ) -> Unit = { _, _, _, -> }
){
    val snackBarHostState = remember { SnackbarHostState() }

    Column {
        //Add icon
        if (grandAddOrgPermission) {
            Button(onClick = goBack,) {
                Text("Add$attributeName")
            }
        }

        // Material3 间隔
        HorizontalDivider()

        // Title
        Text(text = attributeName)
        // 1级
        EditableExposedDropdownMenu(
            menuTip = "请选择1级$attributeName",
            options = firstLevelOptions.map { it.name },
            selectedOption = firstLevelSelected.name,
            onOptionSelected = { add(it, null, null) }
        )

        // 2级
        EditableExposedDropdownMenu(
            menuTip = "请选择2级$attributeName",
            options = secondLevelOptions.map { it.name },
            selectedOption = secondLevelSelected.name,
            onOptionSelected = { add(firstLevelSelected.name, it, null) }
        )

        // 3级
        EditableExposedDropdownMenu(
            menuTip = "请选择3级$attributeName",
            options = thirdLevelOptions.map { it.name },
            selectedOption = thirdLevelSelected.name,
            onOptionSelected = { add(firstLevelSelected.name, secondLevelSelected.name, it) }
        )


    }

    LaunchedEffect(showMessage) {
        showMessage?.getValueOnce()?.let { message ->
            snackBarHostState.showSnackbar(message)
        }
    }



    // 当初始化时，显示Loading组件
    LoadingContainer(
        show = showLoading
    )


}




@Preview(showBackground = true)
@Composable
fun AttributeSheetPreview() {
    val sampleFirstLevelOptions = listOf(
        AttributeEntity.Organization(id = 1, name = "Organization 1"),
        AttributeEntity.Organization(id = 2, name = "Organization 2")
    )

    val sampleSecondLevelOptions = listOf(
        AttributeEntity.Organization(id = 3, name = "Organization 3"),
        AttributeEntity.Organization(id = 4, name = "Organization 4")
    )

    val sampleThirdLevelOptions = listOf(
        AttributeEntity.Organization(id = 5, name = "Organization 5"),
        AttributeEntity.Organization(id = 6, name = "Organization 6")
    )

    var firstLevelSelected by remember { mutableStateOf(sampleFirstLevelOptions[0]) }
    var secondLevelSelected by remember { mutableStateOf(sampleSecondLevelOptions[0]) }
    var thirdLevelSelected by remember { mutableStateOf(sampleThirdLevelOptions[0]) }

    AttributeSheet(
        attributeName = "组织",
        firstLevelOptions = sampleFirstLevelOptions,
        secondLevelOptions = sampleSecondLevelOptions,
        thirdLevelOptions = sampleThirdLevelOptions,
        firstLevelSelected = firstLevelSelected,
        secondLevelSelected = secondLevelSelected,
        thirdLevelSelected = thirdLevelSelected,
        showLoading = false,
        grandAddOrgPermission = true,
        showMessage = Event("这是一个示例消息"),
        goBack = {
            // 示例的回调操作
        },
        add = { firstLevel, secondLevel, thirdLevel ->
            // 处理添加属性的逻辑
            firstLevelSelected = sampleFirstLevelOptions.firstOrNull { it.name == firstLevel } as AttributeEntity.Organization? ?: firstLevelSelected
            secondLevelSelected = sampleSecondLevelOptions.firstOrNull { it.name == secondLevel } as AttributeEntity.Organization? ?: secondLevelSelected
            thirdLevelSelected = sampleThirdLevelOptions.firstOrNull { it.name == thirdLevel } as AttributeEntity.Organization? ?: thirdLevelSelected
        }
    )
}