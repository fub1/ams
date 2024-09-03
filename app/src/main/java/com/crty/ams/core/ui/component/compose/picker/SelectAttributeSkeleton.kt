package com.crty.ams.core.ui.component.compose.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crty.ams.core.ui.compose.picker.AttributeEntity

@Composable
fun SelectAttributeSkeleton(
    attributeType: String = "",
    firstLevelAttribute: List<AttributeEntity>? = listOf(),
    secondLevelAttribute: List<AttributeEntity>? = listOf(),
    thirdLevelAttribute: List<AttributeEntity>? = listOf(),
    firstLevelSelectId: MutableState<Int?> = mutableStateOf(null),
    secondLevelSelectId: MutableState<Int?> = mutableStateOf(null),
    thirdLevelSelectId: MutableState<Int?> = mutableStateOf(null),
    onFirstLevelSelect: (Int) -> Unit = {},
    onSecondLevelSelect: (Int) -> Unit = {},
    onThirdLevelSelect: (Int) -> Unit = {}
    ) {
    val firstLevelSelectedOption = firstLevelAttribute?.find { it.id == firstLevelSelectId.value }?.name ?: ""
    val secondLevelSelectedOption = secondLevelAttribute?.find { it.id == secondLevelSelectId.value }?.name ?: ""
    val thirdLevelSelectedOption = thirdLevelAttribute?.find { it.id == thirdLevelSelectId.value }?.name ?: ""

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "正在选择属性 - $attributeType", modifier = Modifier.padding(bottom = 16.dp))
        ExposedDropdownMenu(
            menuTip = "请选择一级$attributeType",
            options = firstLevelAttribute,
            selectedOption = firstLevelSelectedOption,
            onOptionSelected = {
                firstLevelSelectId.value = it
                onFirstLevelSelect(it)
            }
        )
        if (firstLevelAttribute != null) {
            ExposedDropdownMenu(
                menuTip = "请选择二级$attributeType",
                options = secondLevelAttribute,
                selectedOption = secondLevelSelectedOption,
                onOptionSelected = {
                    secondLevelSelectId.value = it
                    onSecondLevelSelect(it)
                }
            )
        }

        ExposedDropdownMenu(
            menuTip = "请选择三级$attributeType",
            options = thirdLevelAttribute,
            selectedOption = thirdLevelSelectedOption,
            onOptionSelected = {
                thirdLevelSelectId.value = it
                onThirdLevelSelect(it)
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun SelectAttributeSkeletonPreview() {
    SelectAttributeSkeleton(
        attributeType = "Sample Type",
        firstLevelAttribute = listOf(
            AttributeEntity(1, 0,"First Level Option 1"),
            AttributeEntity(2, 0,"First Level Option 2")
        ),
        secondLevelAttribute = listOf(
            AttributeEntity(3, 2,"First Level Option 1"),
            AttributeEntity(4, 2,"First Level Option 2")
        ),
        thirdLevelAttribute = listOf(
            AttributeEntity(5, 4,"First Level Option 1"),
            AttributeEntity(6, 4,"First Level Option 2")
        )
    )
}