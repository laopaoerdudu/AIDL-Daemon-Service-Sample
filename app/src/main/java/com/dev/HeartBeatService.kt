package com.dev

import android.util.Log
import com.dev.daemon.service.BaseHeartBeatService

class HeartBeatService : BaseHeartBeatService() {

    override fun onStartService() {
        Log.i("WWE", "HeartBeatService #onStartService >>>")
    }

    override fun onStopService() {
        Log.i("WWE", "HeartBeatService #onStopService >>>")
    }

    override fun getDelayExecutedMillis(): Long {
        return 0
    }

    override fun getHeartBeatMillis(): Long {
        return 30 * 1000
    }

    override fun onHeartBeat() {
        Log.i("WWE", "HeartBeatService #onHeartBeat >>>")
    }
}