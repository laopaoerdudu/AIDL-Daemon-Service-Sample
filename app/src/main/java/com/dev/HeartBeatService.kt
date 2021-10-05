package com.dev

import com.dev.daemon.service.BaseHeartBeatService

class HeartBeatService : BaseHeartBeatService() {

    override fun onStartService() {
    }

    override fun onStopService() {
    }

    override fun getDelayExecutedMillis(): Long {
        return 0
    }

    override fun getHeartBeatMillis(): Long {
        return 30 * 1000
    }

    override fun onHeartBeat() {
    }
}