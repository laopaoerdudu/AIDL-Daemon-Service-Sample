package com.dev

import android.app.Application
import com.dev.daemon.helper.DaemonHelper

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DaemonHelper.setup(this, HeartBeatService::class.java)
    }
}