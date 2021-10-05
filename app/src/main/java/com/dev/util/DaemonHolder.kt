package com.dev.util

import android.app.Service
import android.content.Context
import kotlin.reflect.KClass

class DaemonHolder(private val mContext: Context? = null, private val mService: KClass<in Service>? = null) {
    init {
        startService()
    }

    companion object {
        fun startService() {

        }
    }
}