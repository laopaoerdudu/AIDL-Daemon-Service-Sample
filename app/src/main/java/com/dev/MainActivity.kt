package com.dev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dev.daemon.helper.DaemonHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnStartService).setOnClickListener {
            DaemonHelper.startService()
        }
        findViewById<Button>(R.id.btnStopService).setOnClickListener {
            DaemonHelper.stopService()
        }
    }
}