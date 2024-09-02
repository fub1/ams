 package com.crty.ams.pda.utils.keyevent

import android.os.RemoteException
import android.util.Log
import com.seuic.scankey.IKeyEventCallback
import com.seuic.scankey.ScanKeyService

class ScanKeyEventReceiver {

    private val mScanKeyService: ScanKeyService = ScanKeyService.getInstance()

    private val mCallback: IKeyEventCallback = object : IKeyEventCallback.Stub() {
        @Throws(RemoteException::class)
        override fun onKeyDown(keyCode: Int) {
            Log.i("Seuic-Key tester", "onKeyDown: keyCode=$keyCode")
        }

        @Throws(RemoteException::class)
        override fun onKeyUp(keyCode: Int) {
            Log.i("Seuic-Key tester", "onKeyUp: keyCode=$keyCode")
        }
    }

    fun registerCall() {
        mScanKeyService.registerCallback(mCallback, "248,249,250,59")
    }

    fun unregisterCall() {
        mScanKeyService.unregisterCallback(mCallback)
    }
}