package com.crty.ams.core.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crty.ams.asset.data.network.model.AssetForGroup
import com.crty.ams.asset.ui.asset_allocation.screen.AssetAllocationScreen
import com.crty.ams.asset.ui.asset_change_batch.screen.AssetChangeBatchScreen
import com.crty.ams.asset.ui.asset_change_single.screen.AssetChangeSingleScreen
import com.crty.ams.asset.ui.asset_check.screen.AssetCheckViewModel
import com.crty.ams.asset.ui.asset_collect.screen.AssetCollectScreen
import com.crty.ams.asset.ui.asset_register.screen.AssetRegisterScreen
import com.crty.ams.core.ui.compose.multilevel_list.MultilevelListScreen
import com.crty.ams.core.ui.compose.picker.AttributeScreen
import com.crty.ams.core.ui.compose.picker.AttributeViewModel
import com.crty.ams.core.ui.compose.roll_list.ComposeScreen
import com.crty.ams.core.ui.screen.HomeScreen
import com.crty.ams.core.ui.screen.LoginScreen
import com.crty.ams.core.ui.screen.LoginSettingsScreen
import com.crty.ams.asset.ui.asset_inventory_detail_confirm.screen.ConfirmDetailScreen
import com.crty.ams.asset.ui.asset_inventory_detail_filter.screen.InventoryDetailFilterScreen
import com.crty.ams.asset.ui.asset_inventory_list.screen.InventoryListScreen
import com.crty.ams.asset.ui.asset_unbinding_ms.screen.AssetUnbindingScreen
import com.crty.ams.core.data.model.AssetInfo
import kotlinx.serialization.json.Json
import org.imaginativeworld.whynotcompose.ui.screens.tutorial.captureimageandcrop.CaptureImageAndCropScreen
import org.imaginativeworld.whynotcompose.ui.screens.tutorial.captureimageandcrop.CaptureImageAndCropViewModel

@Composable
fun AppNavigation(start: RouteList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start.description) {
        composable(route = RouteList.Login.description) {
            LoginScreen(navController)
        }
        composable(route = RouteList.Settings.description) {
            LoginSettingsScreen(navController)
        }
        composable(route = RouteList.Home.description) {
            HomeScreen(navController)
        }
        composable(route = RouteList.Test.description) {
            ComposeScreen(navController)
        }
        composable(route = RouteList.Camera.description) {
            val viewModel: CaptureImageAndCropViewModel = hiltViewModel()

            CaptureImageAndCropScreen(
                viewModel = viewModel,
                goBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = RouteList.Picker.description) {
            val showSheet = remember { mutableStateOf(false) }
            val viewModel: AttributeViewModel = hiltViewModel()
            AttributeScreen(
                viewModel = viewModel
            )
        }
        composable(
            route = RouteList.AssetRegister.description,
            arguments = listOf(
                navArgument("tid") { type = NavType.StringType },
                navArgument("epc") { type = NavType.StringType },
                navArgument("barcode") { type = NavType.StringType }
            )
        ) {backStackEntry ->
            // 获取传递的参数
            val tid = backStackEntry.arguments?.getString("tid") ?: ""
            val epc = backStackEntry.arguments?.getString("epc") ?: ""
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""

            // 创建 AttributePageViewModel，并传递参数
//            val viewModel: AssetRegisterViewModel = hiltViewModel()
//            viewModel.setEpcAndBarcode(tid, epc, barcode)
            AssetRegisterScreen(navController, tid, epc, barcode)
        }

        composable(route = RouteList.AssetCheck.description) {
            AssetCheckViewModel(navController)
        }
        composable(route = RouteList.InventoryList.description) {
            InventoryListScreen(navController)
        }
        composable(route = RouteList.CreateInventory.description) {
            InventoryDetailFilterScreen(navController)
        }
        composable(route = RouteList.ConfirmDetail.description) {
            ConfirmDetailScreen(navController)
        }
        composable(route = RouteList.MultilevelListTest.description) {
            MultilevelListScreen()
        }
        composable(route = RouteList.AssetChangeSingle.description,
            arguments = listOf(
                navArgument("assetInfo") { type = NavType.StringType },
            )
        ) {backStackEntry ->
            val assetInfoJson = backStackEntry.arguments?.getString("assetInfo") ?: ""
            val assetInfo: AssetInfo = Json.decodeFromString(assetInfoJson)
            AssetChangeSingleScreen(navController, assetInfo)
        }
        composable(route = RouteList.AssetChangeBatch.description,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("ids") { type = NavType.StringType },
            )
        ) {backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val assetInfoJson = backStackEntry.arguments?.getString("ids") ?: ""
            val assetIdList: List<Int> = Json.decodeFromString(assetInfoJson)
            AssetChangeBatchScreen(navController, type, assetIdList)
        }
        composable(route = RouteList.AssetAllocation.description,
            arguments = listOf(
                navArgument("ids") { type = NavType.StringType },
            )) {backStackEntry ->
            val assetInfoJson = backStackEntry.arguments?.getString("ids") ?: ""
            val assetIdList: List<Int> = Json.decodeFromString(assetInfoJson)
            AssetAllocationScreen(navController, assetIdList)
        }
        composable(
            route = RouteList.AssetUnbindingMS.description,
            arguments = listOf(
                navArgument("assetsList") { type = NavType.StringType },
            )
        ) {backStackEntry ->
            val assetListJson = backStackEntry.arguments?.getString("assetsList") ?: ""
            val assetList: List<AssetForGroup> = Json.decodeFromString(assetListJson)

            AssetUnbindingScreen(navController, assetList)
        }

        composable(
            route = RouteList.AssetCollect.description,
            arguments = listOf(
                navArgument("ids") { type = NavType.StringType },
            )
        ) {backStackEntry ->
            val assetListJson = backStackEntry.arguments?.getString("ids") ?: ""
            val assetList: List<Int> = Json.decodeFromString(assetListJson)

            AssetCollectScreen(navController, assetList)
        }
    }
}


// 路由列表
// 可以用来关联一个导航标题名称
enum class RouteList(val description: String) {
    Login("login"),
    Settings("settings"),
    Home("home"),
    Test("compose"),
    Camera("camera"),
    Picker("picker"),
    AssetRegister("assetRegister/{tid}/{epc}/{barcode}"),
    AssetCheck("assetCheck"),
    InventoryList("inventoryList"),
    CreateInventory("createInventory"),
    ConfirmDetail("confirmDetail"),
    MultilevelListTest("multilevelListTest"),
    AssetChangeSingle("assetChangeSingle/{assetInfo}"),
    AssetChangeBatch("assetChangeBatch/{type}/{ids}"),
    AssetAllocation("assetAllocation/{ids}"),
    AssetUnbindingMS("assetUnbindingMS/{assetsList}"),/*主从资产解绑*/
    AssetCollect("assetCollect/{ids}"),/* 资产领用 */
}

fun parseAssetList(json: String?): List<AssetForGroup> {
    return if (json != null) {
        Json.decodeFromString(json)
    } else {
        emptyList()
    }
}