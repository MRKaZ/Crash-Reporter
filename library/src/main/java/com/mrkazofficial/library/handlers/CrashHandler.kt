package com.mrkazofficial.library.handlers

import android.content.Context
import com.mrkazofficial.library.task.CrashTask

/**
 * @Project Crash Reporter
 * @Class Handler
 * @Author MRKaZ
 * @Since 7:22 PM, 3/8/2022
 * @Origin Taprobana (LK)
 * @Copyright (c) 2022 MRKaZ. All rights reserved.
 */

class CrashHandler : Thread.UncaughtExceptionHandler {

    private lateinit var exHandler: Thread.UncaughtExceptionHandler
    private lateinit var mContext: Context

    fun initiate(context: Context) {
        this.mContext = context
        CrashTask().initiate(context)
    }

    override fun uncaughtException(thread: Thread, trowable: Throwable) {
        if (CrashTask.isSaveCrash)
            CrashTask.saveCrashReport(throwable = trowable)

        this.exHandler.uncaughtException(thread, trowable)
    }
}