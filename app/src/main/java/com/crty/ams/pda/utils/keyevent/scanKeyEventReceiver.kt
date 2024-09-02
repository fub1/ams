 package com.crty.ams.pda.utils.keyevent

import android.os.RemoteException
import android.util.Log
import com.seuic.scankey.IKeyEventCallback
import com.seuic.scankey.ScanKeyService

class ScanKeyEventReceiver {

    private val mScanKeyService: ScanKeyService = ScanKeyService.getInstance()

    // 可注入方法，用来动态注入按键方法-down
    var onKeyDownAction: (Int) -> Unit = { keyCode ->
        Log.i("ScanKeyEventReceiver-D", "onKeyDown: keyCode=$keyCode")
    }

    var onKeyUpAction: (Int) -> Unit = { keyCode ->
        Log.i("ScanKeyEventReceiver-U", "onKeyDown: keyCode=$keyCode")
    }


    private val mCallback: IKeyEventCallback = object : IKeyEventCallback.Stub() {
        @Throws(RemoteException::class)
        override fun onKeyDown(keyCode: Int) {
            onKeyDownAction(keyCode)
            Log.i("ScanKeyEventReceiver", "onKeyDown: keyCode=$keyCode")
        }

        @Throws(RemoteException::class)
        override fun onKeyUp(keyCode: Int) {
            onKeyUpAction(keyCode)
            Log.i("ScanKeyEventReceiver", "onKeyUp: keyCode=$keyCode")
        }
    }

    fun registerCall() {
        mScanKeyService.registerCallback(mCallback, "248,249,250,59")
    }

    fun unregisterCall() {
        mScanKeyService.unregisterCallback(mCallback)
    }
}