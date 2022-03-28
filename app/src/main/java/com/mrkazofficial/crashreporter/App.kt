package com.mrkazofficial.crashreporter

import android.app.Application
import android.os.Environment
import com.mrkazofficial.library.task.CrashReporter

/**
 * @Project Crash Reporter
 * @Class App
 * @Author MRKaZ
 * @Since 8:03 PM, 3/8/2022
 * @Origin Taprobana (LK)
 * @Copyright (c) 2022 MRKaZ. All rights reserved.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initiate crash reporter
        CrashReporter.initiate(this)
        // TODO : Configuration
        // If you want to save crash log as a .txt file true/false
        // Need WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE permission
        CrashReporter.saveCrashLog(true)
        // You can set a custom directory to save crash log .txt file, Default: App package files directory
        CrashReporter.setCrashReporterSavePath(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).toString()
        )
    }
}