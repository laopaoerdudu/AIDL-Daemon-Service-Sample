package com.dev.daemon.receive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class ScreenBroadcastReceiver : BroadcastReceiver() {
    private var isRegistered = false

    override fun onReceive(context: Context?, intent: Intent?) {
        com.dev.daemon.helper.DaemonHelper.startService()
    }

    fun registerReceiver(context: Context) {
        if (!isRegistered) {
            isRegistered = true
            context.registerReceiver(this, IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON) // 开屏
                addAction(Intent.ACTION_SCREEN_OFF) // 锁屏
                addAction(Intent.ACTION_USER_PRESENT) // 解锁
                addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS) // Home 键
            })
        }
    }

    fun unregisterReceiver(context: Context) {
        if (isRegistered) {
            isRegistered = false
            context.unregisterReceiver(this)
        }
    }
}