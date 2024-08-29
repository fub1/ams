package com.crty.ams.core.ui.compose.picker


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// 可编辑的下拉菜单
// 下拉List中Option数据类型使用AttributeEntity,带有id和name
// 由输入框和下拉菜单组成
// AOSP
// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/ExposedDropdownMenuSamples.kt;l=98?q=EditableExposedDropdownMenuSample&sq=
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    menuTip: String,
    options: List<AttributeEntity>? = listOf(),
    selectedOption: String,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(selectedOption)) }
    textFieldValue = TextFieldValue(selectedOption)
    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }, // 使用!expanded切换状态
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor() // 不再需要传递参数
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                label = { Text(menuTip) }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                options?.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            textFieldValue = TextFieldValue(option.name)
                            onOptionSelected(option.id)
                            expanded = false
                        },
                        text = { Text(text = option.name) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExposedDropdownMenuSamplePreview() {
    ExposedDropdownMenu(
        options = listOf(
            AttributeEntity(1, 0,"Option 1"),
            AttributeEntity(2, 0,"Option 2"),
            AttributeEntity(3, 1,"Option 3"),
            AttributeEntity(4, 2,"Option 4"),
            AttributeEntity(5, 4,"Option 5"),
            AttributeEntity(6, 3,"Option 6"),

        ),
        menuTip = "Select an option",
        selectedOption = "Option 1",
        onOptionSelected = {}
    )
}

