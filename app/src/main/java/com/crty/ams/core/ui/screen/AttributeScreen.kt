package com.crty.ams.core.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.ui.component.attribute.AddAttributeWithCodeSkeleton
import com.crty.ams.core.ui.component.attribute.SelectAttributeSkeleton
import com.crty.ams.core.ui.component.LoadingContainer
import com.crty.ams.core.ui.viewmodel.AttributeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributeScreen(
    attributeType: String,
    showSheet: MutableState<Boolean>,
    // onConfirm: () -> Unit,
    // onCancel: () -> Unit,
    //onBack: () -> Unit,
    viewModel: AttributeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        // Note: Remove the `remember` later. Issue: https://issuetracker.google.com/issues/340582180
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


    LoadingContainer(
        show = state.loading
    )

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = bottomSheetState
    ) {
        when(state.mode) {
            AttributeViewModel.AttributeViewMode.SELECT -> {
                //选择模式
                Column {
                    Button(
                        onClick = {},
                    ) {
                        Text("创建${attributeType}")
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
                }
            }

            AttributeViewModel.AttributeViewMode.CREATE -> {
                AddAttributeWithCodeSkeleton(
                    attributeType = attributeType,
                    firstLevelAttribute = state.firstLevelAttributes,
                    secondLevelAttribute = state.secondLevelAttributes,
                    goBack = { },
                    addAttribute = { attrName, attrCode ->
                        viewModel.addAttribute(attrName, attrCode)
                    }
                )
            }
        }
    }
}
