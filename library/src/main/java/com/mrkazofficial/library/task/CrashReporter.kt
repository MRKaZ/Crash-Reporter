package com.mrkazofficial.library.task

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.mrkazofficial.library.handlers.CrashHandler
import com.mrkazofficial.library.utils.Constants.CHECK_CRASH_LOG

/**
 * @Project Crash Reporter
 * @Class CrashReporter
 * @Author MRKaZ
 * @Since 7:15 PM, 3/8/2022
 * @Origin Taprobana (LK)
 * @Copyright (c) 2022 MRKaZ. All rights reserved.
 */

@SuppressLint("StaticFieldLeak")
class CrashReporter {

    companion object {

        private lateinit var mContext: Context
        private var isSaveCrash = false
        private lateinit var crashReportPath: String

        /**
         * Initiate the Crash Reporter service
         * @param context activity / context
         */
        fun initiate(context: Context) {
            mContext = context
            CrashHandler().initiate(context)
            setupCrashReporter()
        }

        /**
         * Boolean value for id user like to save their crash log
         * @param saveCrash true / false
         */
        fun saveCrashLog(saveCrash: Boolean) {
            isSaveCrash = saveCrash
            CrashTask.isSaveCrash(saveCrash)

            if (isSaveCrash) {
                if (this::crashReportPath.isInitialized) {
                    if (TextUtils.isEmpty(crashReportPath)) {
                        crashReportPath = CrashTask.defaultPath
                    }
                } else {
                    crashReportPath = CrashTask.defaultPath
                }
            }
        }

        /**
         * If user like to save log, Then set custom path to save path
         * * Default path == Cache dir..
         * @param savePath custom path
         */
        fun setCrashReporterSavePath(savePath: String) {
            if (isSaveCrash) {
                if (!TextUtils.isEmpty(savePath)) {
                    CrashTask.setSavePath(savePath)
                    crashReportPath = savePath
                } else {
                    crashReportPath = CrashTask.defaultPath
                    Log.e(
                        CHECK_CRASH_LOG,
                        "Custom crash log save path not set. Default path setup to : ${CrashTask.defaultPath}"
                    )
                }
            }
        }

        /**
         * Setup crash reporter service
         */
        private fun setupCrashReporter() {
            if (Thread.getDefaultUncaughtExceptionHandler() !is CrashHandler) {
                Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
            }
        }

        /**
         * Exception of the app
         * @param exception fatal exception
         */
        fun exception(exception: Exception) {
            CrashTask.logException(exception)
        }

        fun getCrashReportPath(): String {
            return crashReportPath
        }
    }
}