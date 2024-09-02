package com.crty.ams.pda.utils.barcode//package com.xyx.seuicpda.utils.barcode
//
//import android.content.Context
//import android.util.Log
//import com.rscja.barcode.BarcodeDecoder
//import com.rscja.barcode.BarcodeFactory
//import com.rscja.deviceapi.entity.UHFTAGInfo
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import javax.inject.Inject
//
//class BarcodeScanManager  @Inject constructor() {
//
//    private val mBarcode: BarcodeDecoder = BarcodeFactory.getInstance().barcodeDecoder
//
//    // MutableSharedFlow 用于发射 String 扫码结果
//    private val _scanedTagsStateFlow = MutableStateFlow<String>("")
//    val scanedTagsStateFlow: StateFlow<String> = _scanedTagsStateFlow
//
//
//
//    suspend fun intibarcodescan(context: Context) {
//        mBarcode.open(context)
//        // sdk要求必须延迟1s
//        delay(1000)
//        mBarcode.setDecodeCallback { barcode ->
//            // 扫码结果回调
//            Log.i("BarcodeScanManager", "Barcode received in callback:${barcode.barcodeData}")
//            _scanedTagsStateFlow.value = barcode.barcodeData
//            Log.i("BarcodeScanManager", "Barcode save to flow:${_scanedTagsStateFlow.value}")
//        }
//    }
//
//
//    fun startScan() {
//        mBarcode.startScan()
//    }
//
//    fun stopScan() {
//        mBarcode.stopScan()
//    }
//
//    fun close() {
//        mBarcode.close()
//    }
//}