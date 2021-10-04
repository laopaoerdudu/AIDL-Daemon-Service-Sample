package com.dev

import android.app.Service
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import java.util.*

abstract class HeartBeatService : Service() {
    private val timer = Timer()

    private val timerTask = object : TimerTask() {
        override fun run() {
            onHeartBeat()
        }
    }

    private val binder = object : ServiceManager.Stub() {
        override fun startService() {
            // TODO
        }

        override fun stopService() {
            // TODO
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            try {
                iBinder?.linkToDeath({
                    binder.startService()
                    startBindService()
                }, 1)
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            try {
                binder.stopService()
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }
        }

        override fun onBindingDied(name: ComponentName?) {
            onServiceDisconnected(name)
        }
    }

    abstract fun onHeartBeat()

    private fun startBindService() {
        //startService(Intent(this, /**DaemonService.class**/))

    }
}