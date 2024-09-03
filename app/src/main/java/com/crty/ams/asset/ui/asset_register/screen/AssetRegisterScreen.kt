package com.crty.ams.asset.ui.asset_register.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.MotionEvent
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.crty.ams.R
import com.crty.ams.asset.ui.asset_register.viewmodel.AssetRegisterViewModel
import com.crty.ams.asset.ui.asset_unbinding_ms.viewmodel.AssetUnbindingViewModel
import com.crty.ams.core.ui.compose.camera2.rememberImagePainter
import com.crty.ams.core.ui.compose.picker.AttributePage
import com.crty.ams.core.ui.compose.picker.AttributeViewModel
import com.crty.ams.core.ui.theme.AmsTheme
import java.util.Calendar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetRegisterScreen(
    navController: NavHostController,
    tid: String,
    epc: String,
    barcode: String,
    viewModel: AssetRegisterViewModel = hiltViewModel(),
    ) {
    val topBar = stringResource(R.string.asset_screen_assetRegisterScreen_topBar)

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
    val remark = asset.remark
    // Collect other inputs similarly...

    val isLoading by viewModel.isLoading.collectAsState()
    val isTimeout by viewModel.isTimeout.collectAsState()
    val isFailed by viewModel.isFailed.collectAsState()
    val failedMessage by viewModel.failedMessage.collectAsState()
    val isError by viewModel.isError.collectAsState()


    var codeError by remember { mutableStateOf(false) }
    var codeLabel by remember { mutableStateOf("* 请输入资产代码") }
    var nameError by remember { mutableStateOf(false) }
    var nameLabel by remember { mutableStateOf("* 请输入资产名称") }
    var categoryError by remember { mutableStateOf(false) }
    var categoryLabel by remember { mutableStateOf("* 请选择资产分类") }
    var brandError by remember { mutableStateOf(false) }
    var brandLabel by remember { mutableStateOf("* 请输入品牌") }
    var modelError by remember { mutableStateOf(false) }
    var modelLabel by remember { mutableStateOf("* 请输入型号") }
    var priceError by remember { mutableStateOf(false) }
    var priceLabel by remember { mutableStateOf("请输入价格") }

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

    LaunchedEffect(Unit) {
        viewModel.setEpcAndBarcode(tid, epc, barcode)
    }

    fun submit(){
        var isFill = true

        if (assetCode.isEmpty()){
            codeError = true
            codeLabel = "资产代码不能为空"
            isFill = false
        }
        if (assetName.isEmpty()){
            nameError = true
            nameLabel = "资产名称不能为空"
            isFill = false
        }
        if (assetCategory.isEmpty()){
            categoryError = true
            categoryLabel = "资产分类不能为空"
            isFill = false
        }
        if (brand.isEmpty()){
            brandError = true
            brandLabel = "品牌不能为空"
            isFill = false
        }
        if (model.isEmpty()){
            modelError = true
            modelLabel = "型号不能为空"
            isFill = false
        }
        if (price.isEmpty()){
            viewModel.onPriceChanged("0")
        }else{
            if (!isNumeric(price)){
                priceError = true
                priceLabel = "请输入数字"
                isFill = false
            }
        }

        if (isFill){
            viewModel.submit()
        }


    }




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
                    submit()
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
                label = codeLabel,
                value = assetCode,
                onValueChange = {
                    viewModel.onAssetCodeChanged(it)
                    codeLabel = "* 请输入资产代码"
                                },
                onClick = {
                    // Handle click event here

                },
                codeError
            )
            TextFieldWithLabel(
                text = "资产名称",
                label = nameLabel,
                value = assetName,
                onValueChange = {
                    viewModel.onAssetNameChanged(it)
                    nameLabel = "* 请输入资产名称"
                                },
                onClick = {
                    // Handle click event here

                },
                nameError
            )
            // Add other TextFieldWithLabel here for input3 to input9...
            TextFieldWithLabel(
                text = "资产分类",
                label = categoryLabel,
                value = assetCategory,
                onValueChange = {
                    viewModel.onAssetCategoryChanged(it)
                    categoryLabel = "* 请输入资产分类"
                                },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 3 clicked", Toast.LENGTH_SHORT).show()
//                    System.err.println("Input 3: $input3")
                    selectedAttributeType.value = "资产分类"
                    showSheet.value = true
                },
                categoryError
            )
            TextFieldWithLabel(
                text = "品牌",
                label = brandLabel,
                value = brand,
                onValueChange = {
                    viewModel.onBrandChanged(it)
                    brandLabel = "* 请输入品牌"
                                },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 4 clicked", Toast.LENGTH_SHORT).show()
                },
                brandError
            )
            TextFieldWithLabel(
                text = "型号",
                label = modelLabel,
                value = model,
                onValueChange = {
                    viewModel.onModelChanged(it)
                    modelLabel = "* 请输入型号"
                                },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 5 clicked", Toast.LENGTH_SHORT).show()
                },
                modelError
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
                label = priceLabel,
                value = price,
                onValueChange = { viewModel.onPriceChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 9 clicked", Toast.LENGTH_SHORT).show()
                    viewModel.onPriceChanged("")
                },
                priceError
            )
            TextFieldWithLabel(
                text = "备注",
                label = "请输入备注",
                value = remark,
                onValueChange = { viewModel.onRemarkChanged(it) },
                onClick = {
                    // Handle click event here
//                    Toast.makeText(context, "Input 10 clicked", Toast.LENGTH_SHORT).show()
                },
                false
            )

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 32.dp, top = 32.dp, end = 32.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                painter = rememberImagePainter(
                    data = null,
                    placeholder = R.drawable.default_placeholder
                ),
                contentDescription = "Image",
                contentScale = ContentScale.Crop
            )


            // Display the ModalBottomSheet
            if (showSheet.value) {
                AttributePage(
                    attributeType = selectedAttributeType.value,
                    showSheet = showSheet,
                    navController,
                    onDismiss = { data, number ->
                        // 回传数据给 AssetRegisterScreen 的 ViewModel
                        viewModel.updateAssetCategoryId(data, number)
                        showSheet.value = false // 关闭 ModalBottomSheet
                    }
                )
            }
            // 显示弹窗
            if (showSuccessPopup) {
                SuccessPopup()
            }
        }
        // 加载动画
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()  // 显示加载动画
            }
        }
        if (isTimeout) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissTimeoutDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissTimeoutDialog() }) {
                        Text("确定")
                    }
                },
                title = { Text("请求超时") },
                text = { Text("请求登记接口超时，请重试。") }
            )
        }
        if (isFailed) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissFieldDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissFieldDialog() }) {
                        Text("确定")
                    }
                },
                title = { Text("登记失败") },
                text = { Text(failedMessage) }
            )
        }
        if (isError) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissErrorDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissErrorDialog() }) {
                        Text("确定")
                    }
                },
                title = { Text("操作异常") },
                text = { Text("登记过程中出现异常。") }
            )
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
                .pointerInteropFilter {
                    if (it.action == MotionEvent.ACTION_DOWN) {
                        onClick()
                    }
                    false
                },
            label = {
                Text(
                    label
                )
            },
            singleLine = true,
            isError = isRequired
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

fun isNumeric(value: String): Boolean {
    return value.toDoubleOrNull() != null
}