package com.dev.daemon.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build

class DaemonUtil {
    companion object {
        const val INTERVAL_TIME: Long = 30 * 1000
        private val BRAND: String = Build.BRAND.lowercase()

        fun isXiaomi(): Boolean = "xiaomi" == Build.MANUFACTURER.lowercase()

        fun isViVo(): Boolean = BRAND.contains("vivo") || BRAND.contains("bbk")

        fun isOppo(): Boolean = BRAND.contains("oppo")

        fun isHuawei(): Boolean = BRAND.contains("huawei") || BRAND.contains("honor")

        fun isServiceRunning(context: Context, serviceName: String): Boolean {
            getActivityManager(context)?.getRunningServices(100)?.forEach { serviceInfo ->
                if (serviceName == serviceInfo.service.className) {
                    return true
                }
            }
            return false
        }

        fun isProcessRunning(context: Context): Boolean {
            return isProcessRunning(context, getProcessName(context))
        }

        private fun getActivityManager(context: Context): ActivityManager? {
            return context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        }

        private fun getProcessName(context: Context): String {
            getActivityManager(context)?.runningAppProcesses?.forEach { processInfo ->
                if (android.os.Process.myPid() == processInfo.pid) {
                    return processInfo.processName
                }
            }
            return ""
        }

        private fun isProcessRunning(context: Context, processName: String): Boolean {
            getActivityManager(context)?.runningAppProcesses?.forEach { processInfo ->
                if (processName == processInfo.processName) {
                    return true
                }
            }
            return false
        }
    }
}