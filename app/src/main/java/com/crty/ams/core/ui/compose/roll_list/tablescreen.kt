package com.crty.ams.core.ui.compose.roll_list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class MenuItem(var name: String, var unitPrice: Int)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SimpleTable(menuItems: List<MenuItem>) {
    var editingIndex by remember { mutableStateOf<Pair<Int, Boolean>?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            //.heightIn(max = with(density) { constraints.maxHeight.toDp() } * 0.65f)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Name",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Price",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(menuItems) { index, menuItem ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        if (editingIndex == Pair(index, true)) {
                            TextField(
                                value = menuItem.name,
                                onValueChange = { menuItem.name = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(max = 56.dp),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    keyboardController?.hide()
                                    editingIndex = null
                                }),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                //keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                            )
                        } else {
                            Text(
                                text = menuItem.name,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { editingIndex = Pair(index, true) },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        if (editingIndex == Pair(index, false)) {
                            TextField(
                                value = menuItem.unitPrice.toString(),
                                onValueChange = { menuItem.unitPrice = it.toIntOrNull() ?: menuItem.unitPrice },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(max = 56.dp),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Center),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    keyboardController?.hide()
                                    editingIndex = null
                                }),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                //keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                            )
                        } else {
                            Text(
                                text = "$${menuItem.unitPrice}",
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { editingIndex = Pair(index, false) },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleTablePreview() {
    val menuItems = remember { mutableStateListOf(MenuItem("Apple", 10), MenuItem("Banana", 20)) }
    SimpleTable(menuItems)
}