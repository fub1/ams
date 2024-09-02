package com.crty.ams.pda.utils.deviceinfo

import android.annotation.SuppressLint

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.seuic.misc.Misc
import com.crty.ams.pda.utils.rfid.isRfidAvailable


fun verifyDevice(): Boolean {
    // seuic 品牌名可能会变，所以不验证品牌名
    return (getDeviceSN() !="Unknown" && isRfidAvailable())
}


@SuppressLint("HardwareIds")
fun getAndroidId(context: Context): String {
    val androidID: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    Log.i("Get Android ID", androidID)
    return androidID
}

fun getBrand(): String {
    val deviceBRAND: String = android.os.Build.BRAND
    Log.i("Get Device Model", deviceBRAND)
    return deviceBRAND
}

fun getDeviceModel(): String {
    val deviceModel: String = android.os.Build.MODEL
    Log.i("Get Device Model", deviceModel)
    return deviceModel
}

fun getDeviceSN(): String {
    try {
        val mMisc = Misc()
        val deviceSN: String = mMisc.sn
        Log.i("Get Device SN", deviceSN)
        return deviceSN
    } catch (e: Exception) {
        Log.e("Get Device SN", e.toString())
        return "Unknown"
    }
}