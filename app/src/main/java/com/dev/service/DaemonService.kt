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

        // TODO: DaemonHolder.restartService(getApplicationContext(), getClass());

        screenBroadcastReceiver.unregisterReceiver(this)
    }

    private fun listenNetworkConnectivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.requestNetwork(
                NetworkRequest.Builder().build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        // TODO: DaemonHolder.startService()
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        // TODO:  DaemonHolder.startService();
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        // TODO: DaemonHolder.startService();
                    }
                }
            )
        }
    }

    private fun startBindService() {
        // TODO: if (DaemonHolder.mService != null) {
        //                startService(new Intent(this, DaemonHolder.mService));
        //                bindService(new Intent(this, DaemonHolder.mService), serviceConnection, Context.BIND_IMPORTANT);
        //            }
    }
}