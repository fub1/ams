package com.crty.ams.core.ui.compose.picker

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

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributePage(
    attributeType: String,
    showSheet: MutableState<Boolean>,
    viewModel: AttributeViewModel = hiltViewModel()
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
                    )

                    Button(
                        onClick = { showSheet.value = false },
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
                    goBack = { viewModel.toAttributeSelectMode() },
                    addAttribute = { attrName, attrCode ->
                        viewModel.addAttribute(attrName, attrCode)
                    }
                )
            }
        }
    }
}