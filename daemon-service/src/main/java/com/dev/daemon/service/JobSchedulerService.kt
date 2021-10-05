package com.dev.daemon.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import com.dev.daemon.helper.DaemonHelper
import com.dev.daemon.util.DaemonUtil

class JobSchedulerService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        DaemonHelper.startService()
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        DaemonHelper.startService()
        return false
    }

    fun scheduleJobService(context: Context) {
        var isSuccess = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder =
                JobInfo.Builder(10000, ComponentName(context, JobSchedulerService::class.java))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.apply {
                    setMinimumLatency(DaemonUtil.INTERVAL_TIME)
                    setOverrideDeadline(DaemonUtil.INTERVAL_TIME * 2)
                    setBackoffCriteria(
                        DaemonUtil.INTERVAL_TIME,
                        JobInfo.BACKOFF_POLICY_LINEAR
                    ) // 线性重试方案
                }
            } else {
                builder.setPeriodic(DaemonUtil.INTERVAL_TIME)
            }
            builder.setPersisted(true)
            (context.getSystemService(JOB_SCHEDULER_SERVICE) as? JobScheduler)?.apply {
                cancelAll()
                isSuccess = (schedule(builder.build()) == JobScheduler.RESULT_SUCCESS)
            }
        }
        if (isSuccess) {
            Log.i("WWE", "JobSchedulerService Scheduler Success!")
        } else {
            Log.i("WWE", "JobSchedulerService Scheduler Failed!")
        }
    }
}