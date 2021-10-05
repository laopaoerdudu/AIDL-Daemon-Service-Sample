package com.dev.helper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import com.dev.service.HeartBeatService
import com.dev.util.DaemonUtil
import com.dev.util.safeLeft

object DaemonHelper {
    private var mContext: Context? = null
    private var mService: Class<out Service?>? = null

    fun setup(context: Context, service: Class<out HeartBeatService>) {
        mContext = context
        mService = service
        startService()
    }

    @SuppressLint("StaticFieldLeak")
    fun startService() {
        safeLeft(mContext, mService) { context, service ->
            if (!DaemonUtil.isServiceRunning(context, service.canonicalName)) {
                context.startService(Intent(context, service))
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !DaemonUtil.isXiaomi()) {
                // TODO: JobSchedulerService.scheduleJobService(DaemonHolder.mContext)
            }

            context.packageManager.setComponentEnabledSetting(
                ComponentName(context.packageName, service.name),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    fun stopService() {
        safeLeft(mContext, mService) { context, service ->
            if (DaemonUtil.isServiceRunning(context, service.canonicalName)) {
                context.stopService(Intent(context, service))
            }
        }
    }

    fun restartService(context: Context, cls: Class<*>) {
        val pendingIntent = PendingIntent.getService(context, 1, Intent(context, cls).apply {
            `package` = context.packageName
        }, PendingIntent.FLAG_ONE_SHOT)
        (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + DaemonUtil.INTERVAL_TIME,
            pendingIntent
        )
    }
}