package com.crty.ams.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun WheelPicker(
    listState: LazyListState,
    items: List<String>,
    onItemSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val centerIndex = remember { mutableIntStateOf(0) }

    Box(
        modifier
            .height(200.dp)
            .width(100.dp)
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 80.dp), // 80.dp 的 padding 用来模拟中间预选区
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, item ->
                Text(
                    text = item,
                    fontSize = 18.sp,
                    fontWeight = if (index == centerIndex.intValue) FontWeight.Bold else FontWeight.Light,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .animateContentSize()
                )
            }
        }

        // 上下两条线，中间透明
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(25.dp) // 与 Box 的高度一致
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
                        abs((it.offset + it.size / 2) - viewportCenter)
                    }
                }
                .collect { centerItem ->
                    centerItem?.let {
                        centerIndex.intValue = it.index
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
                                abs((it.offset + it.size / 2) - viewportCenter)
                            }
                            centerItem?.let {
                                listState.animateScrollToItem(it.index)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWheelPicker() {
    val sampleItems = List(20) { "Item $it" }
    val listState = rememberLazyListState()

    WheelPicker(
        listState = listState,
        items = sampleItems,
        onItemSelected = { index -> println("Selected item index: $index") }
    )
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheets(){
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show bottom sheet") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )
        }
    ) {
        // Screen content
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                WheelPicker(
                    listState = rememberLazyListState(),
                    items = List(100) { "Item $it" },
                    onItemSelected = { index -> println("Selected item index: $index") }
                )
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBottomSheets() {
    BottomSheets()
}