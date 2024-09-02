package com.crty.ams.pda.utils.rfid//package com.xyx.seuicpda.utils.rfid
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import com.crty.ams.pda.utils.rfid.RfidScanManager
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class RfidScanKeyEventReceiver(private val coroutineScope: CoroutineScope) : BroadcastReceiver() {
//
//    companion object {
//        private const val ACTION_KEY_UP = "com.rscja.android.KEY_UP"
//        private const val ACTION_KEY_DOWN = "com.rscja.android.KEY_DOWN"
//        private const val KEY_CODE_SIDESCANKEY = 293
//        private const val KEY_CODE_MAINSCANKEY = 139
//    }
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val action = intent.action
//        val keyCode: Int = intent.getIntExtra("keycode", -1)
//
//        Log.i("KeyEventReceiver", "Received action: $action, keyCode: $keyCode")
//
//        if (keyCode != -1) {
//            coroutineScope.launch {
//                handleKeyEvent(context, keyCode, action)
//            }
//        } else {
//            Log.i("KeyEventReceiver", "Invalid keycode received")
//        }
//    }
//
//    private suspend fun handleKeyEvent(context: Context, keyCode: Int, action: String?) {
//        when (keyCode) {
//            KEY_CODE_SIDESCANKEY -> handleKeyAction(context, KEY_CODE_SIDESCANKEY, action)
//            KEY_CODE_MAINSCANKEY -> handleKeyAction(context, KEY_CODE_MAINSCANKEY, action)
//            else -> Log.i("KeyEventReceiver", "Unhandled keycode: $keyCode")
//        }
//    }
//
//    private suspend fun handleKeyAction(context: Context, keyCode: Int, action: String?) {
//        when (action) {
//            ACTION_KEY_UP -> {
//                Log.i("KeyEventReceiver", "send key code event up $keyCode")
//                performKeyUpTask(context, keyCode)
//            }
//            ACTION_KEY_DOWN -> {
//                Log.i("KeyEventReceiver", "send key code event down $keyCode")
//                performKeyDownTask(context, keyCode)
//            }
//            else -> Log.i("KeyEventReceiver", "Unhandled action: $action")
//        }
//    }
//
//    private suspend fun performKeyUpTask(context: Context, keyCode: Int) {
//        delay(10)
//        Log.i("KeyEventReceiver", "Performing Key Up Task for keyCode $keyCode")
//        RfidScanManager().stopInventoryTag()
//    }
//
//    private suspend fun performKeyDownTask(context: Context, keyCode: Int) {
//        delay(120)
//        Log.i("KeyEventReceiver", "Performing Key Down Task for keyCode $keyCode")
//        RfidScanManager().startInventoryTag()
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//fun registerKeyEventReceiver(context: Context, receiver: RfidScanKeyEventReceiver) {
//    Log.i("KeyEventReceiver", "Registering KeyEventReceiver")
//    val filter = IntentFilter().apply {
//        addAction("com.rscja.android.KEY_UP")
//        addAction("com.rscja.android.KEY_DOWN")
//    }
//    context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
//}
//
//fun unregisterKeyEventReceiver(context: Context, receiver: RfidScanKeyEventReceiver) {
//    Log.i("KeyEventReceiver", "Unregistering KeyEventReceiver")
//    context.unregisterReceiver(receiver)
//}