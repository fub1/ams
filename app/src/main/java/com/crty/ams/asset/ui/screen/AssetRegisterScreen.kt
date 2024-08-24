package com.crty.ams.asset.ui.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.MotionEvent
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.crty.ams.R
import com.crty.ams.asset.ui.viewmodel.AssetRegisterViewModel
import com.crty.ams.core.ui.compose.picker.AttributePage
import com.crty.ams.core.ui.compose.picker.AttributeViewModel
import com.crty.ams.core.ui.theme.AmsTheme
import java.util.Calendar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetRegisterScreen(navController: NavHostController, viewModel: AssetRegisterViewModel = hiltViewModel(), attributeViewModel: AttributeViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_assetRegisterScreen_topBar)

//    val assetCode by viewModel.asset_code.collectAsState()
//    val assetName by viewModel.asset_name.collectAsState()
//    val assetCategory by viewModel.asset_category.collectAsState()
//    val brand by viewModel.brand.collectAsState()
//    val model by viewModel.model.collectAsState()
//    val sn by viewModel.sn.collectAsState()
//    val supplier by viewModel.supplier.collectAsState()
//    val purchaseDate by viewModel.purchase_date.collectAsState()
//    val price by viewModel.price.collectAsState()
    val asset by viewModel.asset.collectAsState()
    val assetCode = asset.asset_code
    val assetName = asset.asset_name
    val assetCategory = asset.asset_category
    val brand = asset.brand
    val model = asset.model
    val sn = asset.sn
    val supplier = asset.supplier
    val purchaseDate = asset.purchase_date
    val price = asset.price
    // Collect other inputs similarly...

    // Get the context here
    val context = LocalContext.current

    // State to control the visibility of the ModalBottomSheet
    val showSheet = remember { mutableStateOf(false) }
    val selectedAttributeType = remember { mutableStateOf("") }


    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // State to hold the selected date
    val selectedDate = remember { mutableStateOf("") }//日期选择器

    // 监听 ViewModel 中的弹窗显示状态
    val showSuccessPopup by viewModel.showSuccessPopup





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
        },
        floatingActionButton = {
            AmsTheme{
                FloatingActionButton(onClick = {
                    // Handle FAB click action here
                    viewModel.submit()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Submit")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End // Position FAB to the end (bottom-right) by default
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
                    viewModel.onPurchaseDateChanged(formattedDate) // Update ViewModel with selected date
                }, year, month, day
            )

            TextFieldWithLabel(
                text = "资产代码",
                label = "请输入资产代码",
                value = assetCode,
                onValueChange = { viewModel.onAssetCodeChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 1 clicked", Toast.LENGTH_SHORT).show()
//                    System.err.println("Input 1: $input1")
//
//                    selectedAttributeType.value = "Attribute Type 1"
//                    showSheet.value = true
                },
                true
            )
            TextFieldWithLabel(
                text = "资产名称",
                label = "请输入资产名称",
                value = assetName,
                onValueChange = { viewModel.onAssetNameChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 2 clicked", Toast.LENGTH_SHORT).show()
//                    System.err.println("Input 2: $input2")
//
//                    selectedAttributeType.value = "Attribute Type 2"
//                    showSheet.value = true
                },
                true
            )
            // Add other TextFieldWithLabel here for input3 to input9...
            TextFieldWithLabel(
                text = "资产分类",
                label = "请选择资产分类",
                value = assetCategory,
                onValueChange = { viewModel.onAssetCategoryChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 3 clicked", Toast.LENGTH_SHORT).show()
//                    System.err.println("Input 3: $input3")
                    selectedAttributeType.value = "资产分类"
                    showSheet.value = true
                },
                true
            )
            TextFieldWithLabel(
                text = "品牌",
                label = "请输入品牌",
                value = brand,
                onValueChange = { viewModel.onBrandChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 4 clicked", Toast.LENGTH_SHORT).show()
                },
                true
            )
            TextFieldWithLabel(
                text = "型号",
                label = "请输入型号",
                value = model,
                onValueChange = { viewModel.onModelChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 5 clicked", Toast.LENGTH_SHORT).show()
                },
                true
            )
            TextFieldWithLabel(
                text = "序列号",
                label = "请输入序列号",
                value = sn,
                onValueChange = { viewModel.onSnChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 6 clicked", Toast.LENGTH_SHORT).show()
                },
                false
            )
            TextFieldWithLabel(
                text = "供应商",
                label = "请输入供应商",
                value = supplier,
                onValueChange = { viewModel.onSupplierChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 7 clicked", Toast.LENGTH_SHORT).show()
                },
                false
            )
            TextFieldWithLabel(
                text = "采购日期",
                label = "请选择采购日期",
                value = purchaseDate,
                onValueChange = { viewModel.onPurchaseDateChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 8 clicked", Toast.LENGTH_SHORT).show()
                    datePickerDialog.show()
                },
                false
            )
            TextFieldWithLabel(
                text = "价格",
                label = "请输入价格",
                value = price,
                onValueChange = { viewModel.onPriceChanged(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 9 clicked", Toast.LENGTH_SHORT).show()
                },
                false
            )


            // Display the ModalBottomSheet
            if (showSheet.value) {
                AttributePage(
                    attributeType = selectedAttributeType.value,
                    showSheet = showSheet
                )
            }
            // 显示弹窗
            if (showSuccessPopup) {
                SuccessPopup()
            }
        }
    }




}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldWithLabel(
    text: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    isRequired: Boolean // 添加一个布尔参数来表示是否必填
) {
    Column {
        Text(text = text, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
//                .clickable { onClick() }, // Clickable modifier
                .pointerInteropFilter {
                    if (it.action == MotionEvent.ACTION_DOWN) {
                        onClick()
                    }
                    false
                },
            label = {
                Text(
                    text = if (isRequired) " *$label" else "" + label, // 在标签旁边加上星号
                    color = if (isRequired && value.isEmpty()) MaterialTheme.colorScheme.error else Color.Unspecified // 可选：用红色显示必填未填写的提示
                )
            },
            singleLine = true,
            isError = isRequired && value.isEmpty() // 如果是必填项但未填写，则标记为错误
        )
    }
}

@Composable
fun SuccessPopup() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false // 允许我们自定义 Dialog 的宽度
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x15000000)) // 设置 Dialog 的半透明背景
                .padding(32.dp), // 设置内容与边缘的距离
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Lottie 动画
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = 1
                    )
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 显示 "注册成功" 的文本
                    Text(text = "注册成功", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}