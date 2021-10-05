package com.dev.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.IBinder
import android.os.RemoteException
import com.dev.receive.ScreenBroadcastReceiver
import com.dev.ServiceManager
import com.dev.helper.DaemonHelper

class DaemonService : Service() {
    private val screenBroadcastReceiver = ScreenBroadcastReceiver()

    private val aidl = object : ServiceManager.Stub() {
        override fun startService() {
        }

        override fun stopService() {
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            try {
                iBinder?.linkToDeath({
                    aidl.startService()
                    startBindService()
                }, 1)
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            try {
                aidl.stopService()
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }
        }

        override fun onBindingDied(name: ComponentName?) {
            onServiceDisconnected(name)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startBindService()
        listenNetworkConnectivity()
        screenBroadcastReceiver.registerReceiver(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return aidl
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        DaemonHelper.restartService(applicationContext, this.javaClass)
        screenBroadcastReceiver.unregisterReceiver(this)
    }

    private fun listenNetworkConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.requestNetwork(
                NetworkRequest.Builder().build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        DaemonHelper.startService()
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        DaemonHelper.startService()
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        DaemonHelper.startService()
                    }
                }
            )
        }
    }

    private fun startBindService() {
        DaemonHelper.mService?.let { service ->
            startService(Intent(this, service))
            bindService(Intent(this, service), serviceConnection, Context.BIND_IMPORTANT)
        }
    }
}