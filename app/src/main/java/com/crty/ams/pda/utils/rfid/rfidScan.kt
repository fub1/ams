package com.crty.ams.pda.utils.rfid

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.seuic.uhf.UHFService
import com.seuic.uhf.EPC
import com.seuic.uhf.IReadTagsListener
import javax.inject.Inject


// 工具类
class RfidScanManager @Inject constructor (context: Context) {

    // RFID模块实例
    private val mRfid = UHFService.getInstance(context)

    // MutableSharedFlow 用于发射 UHFTAGInfo 对象
    private val _rfidTagsStateFlow = MutableStateFlow<List<EPC>>(emptyList())
    val rfidTagsStateFlow: StateFlow<List<EPC>> = _rfidTagsStateFlow

    // jetpack组件副作用`DisposableEffect`初始化RFID模块
    // 初始化RFID模块后，直接将RFID读取回调注册到flow中
    fun initRfid(context: Context) {
        try {
            mRfid.open()
            Log.i("RFID-init", "RFID is isPowerOn: ${mRfid.isOpen}+${mRfid.region}")

            mRfid.setRegion("FCC")
            mRfid.setPower(33)

            val rfidCallBack = IReadTagsListener { epcList ->
                Log.d("RfidScanManager", "Tag received in callback:${epcList}")
                _rfidTagsStateFlow.value = epcList

                for (epc in epcList) {
                    val epcId = epc.id
                    val rssi = epc.rssi
                    Log.i("RFID-TagRead", "EPC ID: $epcId, RSSI: $rssi")
                }
            }
        } catch (e: NullPointerException) {
            Log.i("RFID-isRfidAvailable", "RFID reader verify not pass")
        }
    }

    // jetpack组件副作用`DisposableEffect`销毁RFID模块
    fun destroyRfid(){
        if (mRfid.isOpen) {
            mRfid.close()

        }
    }

    fun setRfidPower(targetPower: Int): Boolean {
        if (targetPower in 5..33) {

            mRfid.setPower(targetPower)
            Log.i("RFID-setRfidPower", "target power: $targetPower,Real Power:${mRfid.power}")
            return true
        }
        else{
            Log.i("RFID-setRfidPower", "Invalid power value: $targetPower")
            return false
        }
    }

    fun getRfidPower(): Int {
        return mRfid.power
    }

    fun startInventoryTag() {
        if (!mRfid.isOpen) {
            Log.i("RFID-startInventoryTag", "RFID is not powered on")
        }
        Log.i("RFID-startInventoryTag", "startInventoryTag")
        mRfid.inventoryStart()
    }

    fun stopInventoryTag() {
        if (!mRfid.isOpen) {
            Log.i("RFID-stopInventoryTag", "RFID is not powered on")
        }
        Log.i("RFID-stopInventoryTag", "stopInventoryTag")
        mRfid.inventoryStart()
    }


}