package com.crty.ams.core.ui.compose.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// 注册属性页面的骨架
// 注册的属性需要同时注册code
// 最多支持3级属性的创建，但每次提交只能创建一个属性
// 默认创建1级属性
// 可以点击，进入下一级属性的创建模式

@Composable
fun AddAttributeWithCodeSkeleton(
    attributeType: String = "",
    firstLevelAttribute: List<AttributeEntity>? = listOf(),
    secondLevelAttribute: List<AttributeEntity>? = listOf(), //可以为空
    goBack: () -> Unit = {},
    addAttribute: (attrName: String, attrCode: String) -> Unit = { _, _ -> }
    ) {
    val editLevel = rememberSaveable{ mutableIntStateOf(1) } // 编辑的层级，默认为1最大3
    val attrName = rememberSaveable{ mutableStateOf("") } // 输入框的值
    val attrCode = rememberSaveable{ mutableStateOf("") } // 输入框的值

    val selectedFirstLevelId = rememberSaveable{ mutableStateOf<Int?>(null) }   // 一级属性id,int或null
    val selectedSecondLevelId = rememberSaveable{ mutableStateOf<Int?>(null) } // 二级属性id,int或null

    val firstLevelSelectedOption = firstLevelAttribute?.find { it.id == selectedFirstLevelId.value }?.name ?: ""
    val secondLevelSelectedOption = secondLevelAttribute?.find { it.id == selectedSecondLevelId.value }?.name ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "正在注册属性 - $attributeType 第${editLevel.intValue}级", modifier = Modifier.padding(bottom = 16.dp))
        // 标题和按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row {
                if (editLevel.intValue < 3) {
                    Button(
                        onClick = { if (editLevel.intValue < 3) editLevel.intValue++ },
                        enabled = editLevel.intValue < 3
                    ) {
                        Text("注册第${editLevel.intValue+1}级属性")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (editLevel.intValue > 1) {
                    Button(
                        onClick = { if (editLevel.intValue > 1) editLevel.intValue-- },
                        enabled = editLevel.intValue > 1
                    ) {
                        Text("注册第${editLevel.intValue-1}级属性")
                    }
                }
            }
        }

        // 根据层级显示不同的属性选择框
        when (editLevel.intValue) {
            1 -> {
                EditArea(attrName, attrCode)
            }
            2 -> {
                ExposedDropdownMenu(
                    menuTip = "请选择一级属性",
                    options = firstLevelAttribute,
                    selectedOption = firstLevelSelectedOption,
                    onOptionSelected = {
                        selectedFirstLevelId.value = it
                    }
                )
                EditArea(attrName, attrCode)
            }
            3 -> {
                ExposedDropdownMenu(
                    menuTip = "请选择一级属性",
                    options = firstLevelAttribute,
                    selectedOption = firstLevelSelectedOption,
                    onOptionSelected = {
                        selectedFirstLevelId.value = it
                    }
                )
                ExposedDropdownMenu(
                    menuTip = "请选择二级属性",
                    options = secondLevelAttribute!!,
                    selectedOption = secondLevelSelectedOption,
                    onOptionSelected = {
                        selectedSecondLevelId.value = it
                    }
                )
                EditArea(attrName, attrCode)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = goBack) {
                Text("取消")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { addAttribute(attrName.value, attrCode.value) }) {
                Text("提交")
            }
        }
    }
}


@Composable
private fun EditArea(attrName: MutableState<String>, attrCode: MutableState<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = attrName.value,
            onValueChange = { attrName.value = it },
            label = { Text("属性名称") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = attrCode.value,
            onValueChange = { attrCode.value = it },
            label = { Text("属性代码") },
            modifier = Modifier.weight(1f)
        )
    }
}






@Preview(showBackground = true)
@Composable
fun AddAttributeWithCodeSkeletonPreview() {
    AddAttributeWithCodeSkeleton(
        attributeType = "组织机构（demo）",
        firstLevelAttribute = listOf(
            AttributeEntity(1, 0,"First Level Option 1"),
            AttributeEntity(2, 0,"First Level Option 2")
        ),
        secondLevelAttribute = listOf(
            AttributeEntity(3, 2,"First Level Option 1"),
            AttributeEntity(4, 2,"First Level Option 2")
        )
    )
}

@Preview
@Composable
fun EditAreaPreview() {
    val attrName = rememberSaveable { mutableStateOf("Sample Name") }
    val attrCode = rememberSaveable { mutableStateOf("Sample Code") }
    EditArea(attrName = attrName, attrCode = attrCode)
}

