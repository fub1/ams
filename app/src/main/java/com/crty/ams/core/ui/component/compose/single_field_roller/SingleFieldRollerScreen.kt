package com.crty.ams.core.ui.component.compose.single_field_roller

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crty.ams.asset.ui.asset_change_batch.viewmodel.AssetChangeBatchViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleFieldRollerScreen(
    showSheet: MutableState<Boolean>,
    onConfirm: (Int) -> Unit, // Callback to return selected user ID
    assetChangeBatchViewModel: AssetChangeBatchViewModel = hiltViewModel(),
    singleFieldRollerViewModel: SingleRollerViewModel = hiltViewModel()
) {
    var selectedUserIndex by remember { mutableStateOf(0) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = remember {
            { sheetState ->
                sheetState != SheetValue.Hidden
            }
        }
    )

    val fieldList by singleFieldRollerViewModel.userList.collectAsState()

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // 顶部描述文本
            Text(
                text = "请选择用户信息",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 滚轮上方描述文本
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "属性名", modifier = Modifier.padding(bottom = 8.dp))
//                Text(text = "用户编号", modifier = Modifier.padding(bottom = 8.dp))
            }

            // 单个滚轮 - 显示用户名和用户编码
            WheelPicker(
                items = fieldList,
                onItemSelected = { index ->
                    selectedUserIndex = index
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // 底部按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
//                        onConfirm(userList[selectedUserIndex].id) // 返回选择的用户 ID
                        assetChangeBatchViewModel.updateInputText(fieldList[selectedUserIndex])
                        showSheet.value = false
                    }
                ) {
                    Text("确认")
                }
                Button(
                    onClick = {
//                        onCancel()  // 处理取消操作
                        showSheet.value = false
                    }
                ) {
                    Text("取消")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    items: List<String>,
    onItemSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val centerIndex = remember { mutableStateOf(0) }

    Box(
        modifier
            .height(200.dp)
            .width(200.dp) // 调整宽度以适应用户名和用户编码的显示
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, field ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = field,
                        fontSize = 18.sp,
                        fontWeight = if (index == centerIndex.value) FontWeight.Bold else FontWeight.Light,
                    )
//                    Text(
//                        text = user.number,
//                        fontSize = 18.sp,
//                        fontWeight = if (index == centerIndex.value) FontWeight.Bold else FontWeight.Light,
//                    )
                }
            }
        }

        // 上下两条线，中间透明
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(25.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0x80000000)) // 半透明上边线
                    .align(Alignment.TopCenter)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0x80000000)) // 半透明下边线
                    .align(Alignment.BottomCenter)
            )
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .map { visibleItems ->
                    val viewportStartOffset = listState.layoutInfo.viewportStartOffset
                    val viewportEndOffset = listState.layoutInfo.viewportEndOffset
                    val viewportCenter = (viewportEndOffset - viewportStartOffset) / 2 + viewportStartOffset

                    visibleItems.minByOrNull {
                        Math.abs((it.offset + it.size / 2) - viewportCenter)
                    }
                }
                .collect { centerItem ->
                    centerItem?.let {
                        centerIndex.value = it.index
                        onItemSelected(it.index)
                    }
                }
        }

        LaunchedEffect(listState) {
            listState.interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is DragInteraction.Stop, is DragInteraction.Cancel -> {
                        scope.launch {
                            val visibleItems = listState.layoutInfo.visibleItemsInfo
                            val viewportStartOffset = listState.layoutInfo.viewportStartOffset
                            val viewportEndOffset = listState.layoutInfo.viewportEndOffset
                            val viewportCenter = (viewportEndOffset - viewportStartOffset) / 2 + viewportStartOffset

                            val centerItem = visibleItems.minByOrNull {
                                Math.abs((it.offset + it.size / 2) - viewportCenter)
                            }
                            centerItem?.let {
                                listState.animateScrollToItem(it.index)
                                centerIndex.value = it.index
                                onItemSelected(it.index)
                            }
                        }
                    }
                }
            }
        }
    }
}