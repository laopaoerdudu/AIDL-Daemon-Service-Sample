package com.dev.daemon.receive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dev.daemon.helper.DaemonHelper

class DaemonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        DaemonHelper.startService()
    }
}