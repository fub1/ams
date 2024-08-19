package com.crty.ams.core.ui.component


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
// AOSP
// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/ExposedDropdownMenuSamples.kt;l=98?q=EditableExposedDropdownMenuSample&sq=
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableExposedDropdownMenu(
    menuTip: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(selectedOption)) }

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
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            textFieldValue = TextFieldValue(option)
                            onOptionSelected(option)
                            expanded = false
                        },
                        text = { Text(text = option) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExposedDropdownMenuSamplePreview() {
    EditableExposedDropdownMenu(
        options = listOf("Option 1", "Option 2", "Option 3"),
        menuTip = "Select an option",
        selectedOption = "Option 1",
        onOptionSelected = {}
    )
}