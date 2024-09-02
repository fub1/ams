package com.crty.ams.pda.utils.rfid

import android.util.Log
import com.seuic.uhf.UHFService

fun isRfidAvailable(): Boolean {
    Log.i("RFID-isRfidAvailable", "Testing RFID reader")
    return try {
        val testRfidReader = UHFService.getInstance()
        testRfidReader.open()
        Log.i("RFID-firmwareVersion", "{${testRfidReader.firmwareVersion}}")
        testRfidReader.close()
        true
    } catch (e: NullPointerException) {
        Log.i("RFID-isRfidAvailable", "RFID reader verify not pass")
        false
    } catch (e: RuntimeException) {
        Log.e("RFID-isRfidAvailable", "Runtime exception: ${e.message}")
        false
    }
}