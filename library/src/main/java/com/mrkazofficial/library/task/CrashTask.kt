package com.mrkazofficial.library.task

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mrkazofficial.library.utils.Constants
import com.mrkazofficial.library.utils.Constants.CHECK_CRASH_LOG
import com.mrkazofficial.library.utils.ParseStackTrace
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("StaticFieldLeak")
class CrashTask {

    fun initiate(context: Context) {
        mContext = context
    }

    companion object {

        private lateinit var mContext: Context
        private lateinit var crashReportPath: String
        var isSaveCrash = false

        private val logExceptionService: ExecutorService = Executors.newSingleThreadExecutor()
        private val logeExceptionHandler: Handler = Handler(Looper.getMainLooper()!!)

        private val logSaveService: ExecutorService = Executors.newSingleThreadExecutor()
        private val logeSaveHandler: Handler = Handler(Looper.getMainLooper()!!)

        fun isSaveCrash(saveCrash: Boolean) {
            isSaveCrash = saveCrash
        }

        fun setSavePath(savePath: String) {
            crashReportPath = savePath
        }

        private val crashLogTime: String
            get() {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                return dateFormat.format(Date()).replace(":", "-").replace(" ", "-")
            }

        fun saveCrashReport(throwable: Throwable) {

            sendBroadCast(ParseStackTrace(throwable))

            logSaveService.execute {
                logeSaveHandler.post {
                    if (isSaveCrash) {
                        if (this::crashReportPath.isInitialized) {
                            val finalPath: String = crashReportPath.ifEmpty {
                                defaultPath
                            }
                            val filename =
                                crashLogTime + Constants.EXCEPTION_SUFFIX + Constants.LOG_FILE_EXTENSION
                            writeToFile(finalPath, filename, ParseStackTrace(throwable))
                        } else {
                            val filename =
                                crashLogTime + Constants.EXCEPTION_SUFFIX + Constants.LOG_FILE_EXTENSION
                            writeToFile(defaultPath, filename, ParseStackTrace(throwable))
                            Log.e(
                                CHECK_CRASH_LOG,
                                "Custom crash log save path not set. Default path setup to : $defaultPath"
                            )
                        }
                    }
                }
            }
        }

        fun logException(exception: Exception) {

            sendBroadCast(ParseStackTrace(exception))

            //Log.e(TAG, "saveCrashReport: ${exception.message}")

            logExceptionService.execute {
                logeExceptionHandler.post {
                    if (isSaveCrash) {
                        if (this::crashReportPath.isInitialized) {
                            val finalPath: String = crashReportPath.ifEmpty {
                                defaultPath
                            }
                            val filename =
                                crashLogTime + Constants.EXCEPTION_SUFFIX + Constants.LOG_FILE_EXTENSION
                            writeToFile(finalPath, filename, ParseStackTrace(exception))
                        } else {
                            val filename =
                                crashLogTime + Constants.EXCEPTION_SUFFIX + Constants.LOG_FILE_EXTENSION
                            writeToFile(defaultPath, filename, ParseStackTrace(exception))
                            Log.e(
                                CHECK_CRASH_LOG,
                                "Custom crash log save path not set. Default path setup to : $defaultPath"
                            )
                        }
                    }
                }
            }
        }

        private fun writeToFile(path: String, filename: String, crashLog: String) {
            val crashDir = File(path)
            if (!crashDir.exists() || !crashDir.isDirectory) {
                File(path).mkdir()
            }
            val bufferedWriter: BufferedWriter
            try {
                bufferedWriter = BufferedWriter(
                    FileWriter(
                        path + File.separator + filename
                    )
                )
                bufferedWriter.write(crashLog)
                bufferedWriter.flush()
                bufferedWriter.close()
                Log.d(
                    CHECK_CRASH_LOG,
                    "crash report saved in : " + path + File.separator + filename
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val defaultPath: String
            get() {
                val defaultPath = (mContext.getExternalFilesDir(null)!!.absolutePath
                        + File.separator + Constants.CRASH_REPORT_DIR)
                val file = File(defaultPath)
                file.mkdirs()
                return defaultPath
            }

        private fun sendBroadCast(crashReport: String) {
            val intent = Intent(Constants.CRASH_REPORTER_LOCAL_EVENT)
            intent.putExtra(Constants.CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY, crashReport)
            intent.putExtra(Constants.CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY, true)
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
        }
    }
}
