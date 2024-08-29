package com.crty.ams.asset.ui.asset_change_batch.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.MotionEvent
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.asset.ui.asset_change_batch.viewmodel.AssetChangeBatchViewModel
import com.crty.ams.core.ui.component.compose.single_field_roller.SingleFieldRollerScreen
import com.crty.ams.core.ui.compose.picker.AttributePage
import java.util.Calendar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AssetChangeBatchScreen(navController: NavHostController, viewModel: AssetChangeBatchViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_assetChangeBatchScreen_topBar)

    val context = LocalContext.current

    val textFieldValue by viewModel.inputText.collectAsState()

    val attributeValue by viewModel.attributeValue.collectAsState()

    val showSingleRollerSheet = remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // State to hold the selected date
    val selectedDate = remember { mutableStateOf("") }//日期选择器

    // State to control the visibility of the ModalBottomSheet
    val showSheet = remember { mutableStateOf(false) }
    val selectedAttributeType = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(topBar) },
                actions = {
                    IconButton(onClick = { navController.navigate("loginSettings") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) {
            innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(innerPadding), // Apply padding to prevent overlap with topBar
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    selectedDate.value = formattedDate
                    viewModel.updateAttributeValue(formattedDate) // Update ViewModel with selected date
                }, year, month, day
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start // 左对齐
            ) {
                Text(
                    text = "选择变更属性",
                    style = MaterialTheme.typography.titleLarge // 标题样式
                )
                Spacer(modifier = Modifier.height(8.dp)) // 间距
                Text(
                    text = "批量变更一次只能更新一个属性",
                    style = MaterialTheme.typography.bodyMedium // 内容文本样式
                )
                Spacer(modifier = Modifier.height(8.dp)) // 间距
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = {
                        viewModel.updateInputText(it) /* 更新 ViewModel 中的值*/
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInteropFilter {
                            if (it.action == MotionEvent.ACTION_DOWN) {
                                showSingleRollerSheet.value = true
                            }
                            false
                        },
                    label = {
                        Text(
                            "请选择属性"
                        )
                    },
                    singleLine = true,
                )

                Spacer(modifier = Modifier.height(16.dp)) // 间距
                OutlinedTextField(
                    value = attributeValue,
                    onValueChange = { viewModel.updateAttributeValue(it) /* 更新 ViewModel 中的值*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInteropFilter {
                            if (it.action == MotionEvent.ACTION_DOWN) {
                                when(textFieldValue){
                                    "资产分类" -> {
                                        selectedAttributeType.value = "批量修改资产分类"
                                        showSheet.value = true
                                    }
                                    "采购日期" -> datePickerDialog.show()
                                    else -> null
                                }

                            }
                            false
                        },
                    label = {
                        Text(
                            if (textFieldValue.isEmpty()) "在此输入属性值" else "请输入属性：‘$textFieldValue’的值"
                        )
                    },
                    singleLine = true,
                    enabled = textFieldValue.isNotEmpty()
                )
                Spacer(modifier = Modifier.height(40.dp)) // 间距

                Button(
                    onClick = {
                        /* 跳转到批量修改页面 */


                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("确认")
                }
            }

            if (showSingleRollerSheet.value) {
                SingleFieldRollerScreen(
                    showSingleRollerSheet,
                    onConfirm = {

                    }
                )
            }
            // Display the ModalBottomSheet
            if (showSheet.value) {
//                AttributePage(
//                    attributeType = selectedAttributeType.value,
//                    showSheet = showSheet,
//                    navController
//                )
            }

        }


    }
}