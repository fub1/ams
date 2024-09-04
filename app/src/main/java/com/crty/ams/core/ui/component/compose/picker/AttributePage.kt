package com.crty.ams.core.ui.component.compose.picker

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.core.ui.compose.picker.AddAttributeWithCodeSkeleton
import com.crty.ams.core.ui.compose.picker.AttributeViewModel
import com.crty.ams.core.ui.compose.picker.LoadingContainer

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributePage(
    attributeType: String,
    showSheet: MutableState<Boolean>,
    navController: NavHostController,
    viewModel: AttributeViewModel = hiltViewModel(),
    onDismiss: (String, Int) -> Unit, // 回调函数，用于返回数据
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = remember {
            { sheetState ->
                sheetState != SheetValue.Hidden
            }
        }
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setAttributeType(attributeType)
        viewModel.fetchAllAttributes()
    }

    LoadingContainer(show = state.loading)

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = bottomSheetState
    ) {
        when(state.mode) {
            AttributeViewModel.AttributeViewMode.SELECT -> {
                Column {
                    Row {
                        Button(
                            onClick = { viewModel.toAttributeCreateMode() },
                        ) {
                            Text("创建${attributeType}")
                        }
                        Button(
                            onClick = { showSheet.value = false },
                        ) {
                            Text("取消")
                        }
                    }

                    SelectAttributeSkeleton(
                        attributeType = attributeType,
                        firstLevelAttribute = state.firstLevelAttributes,
                        secondLevelAttribute = state.secondLevelAttributes,
                        thirdLevelAttribute = state.thirdLevelAttributes,
                        firstLevelSelectId = mutableStateOf(state.selectedFirstLevelId),
                        secondLevelSelectId = mutableStateOf(state.selectedSecondLevelId),
                        thirdLevelSelectId = mutableStateOf(state.selectedThirdLevelId),
                        onFirstLevelSelect = { viewModel.onFirstLevelSelected(it) },
                        onSecondLevelSelect = { viewModel.onSecondLevelSelected(it) },
                        onThirdLevelSelect = { viewModel.onThirdLevelSelected(it) },
                    )

                    Button(
                        onClick = {
                            println("first: ${state.selectedFirstLevelId}")
                            println("second: ${state.selectedSecondLevelId}")
                            println("third: ${state.selectedThirdLevelId}")

                            // 从三级到一级依次判断是否非空，找到第一个非空的就停止判断
                            when {
                                state.selectedThirdLevelId != null && state.thirdLevelAttributes != null -> {
                                    viewModel.getSelectedInfo(state.selectedThirdLevelId!!, state.thirdLevelAttributes!!)
                                        ?.let { onDismiss(it.name, it.id) }
                                }
                                state.selectedSecondLevelId != null && state.secondLevelAttributes != null -> {
                                    viewModel.getSelectedInfo(state.selectedSecondLevelId!!, state.secondLevelAttributes!!)
                                        ?.let { onDismiss(it.name, it.id) }
                                }
                                state.selectedFirstLevelId != null && state.firstLevelAttributes != null -> {
                                    viewModel.getSelectedInfo(state.selectedFirstLevelId!!, state.firstLevelAttributes!!)
                                        ?.let { onDismiss(it.name, it.id) }
                                }
                                else -> null // 如果所有级别都为空，则返回 null
                            }


                            showSheet.value = false
                        }
                    ) {
                        Text("确认")
                    }
                }
            }

            AttributeViewModel.AttributeViewMode.CREATE -> {
                AddAttributeWithCodeSkeleton(
                    attributeType = attributeType,
                    firstLevelAttribute = state.firstLevelAttributes,
                    secondLevelAttribute = state.secondLevelAttributes,
                    firstLevelSelectId = mutableStateOf(state.selectedFirstLevelId),
                    secondLevelSelectId = mutableStateOf(state.selectedSecondLevelId),
                    onFirstLevelSelect = { viewModel.onFirstLevelSelected(it) },
                    onSecondLevelSelect = { viewModel.onSecondLevelSelected(it) },
                    goBack = { viewModel.toAttributeSelectMode() },
                    addAttribute = { attrName, attrCode ->
                        viewModel.addAttribute(attrName, attrCode)
                    }
                )
            }
        }
    }
}