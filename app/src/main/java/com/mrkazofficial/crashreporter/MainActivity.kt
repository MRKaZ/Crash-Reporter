package com.mrkazofficial.crashreporter

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mrkazofficial.library.task.CrashReporter
import com.mrkazofficial.library.task.CrashReporter.Companion.getCrashReportPath
import com.mrkazofficial.library.utils.Constants.CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY
import com.mrkazofficial.library.utils.Constants.CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY
import com.permissionx.guolindev.PermissionX
import com.mrkazofficial.crashreporter.dialogs.DialogCrashReport
import com.mrkazofficial.library.utils.Constants.CRASH_REPORTER_LOCAL_EVENT

open class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionList: List<String> = arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        PermissionX.init(this)
            .permissions(permissionList)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Read and Write permission required. For if you saving reports.",
                    "Ok",
                    "Cancel"
                )

            }.request { allGranted, _, deniedList ->
                if (!allGranted) {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


        findViewById<Button>(R.id.btnIndexOut).setOnClickListener {
            // Index out bound exception
            try {
                val list: ArrayList<String> = ArrayList()
                list.add("IOB")
                val iob = list[2]
                Log.e(tag, "onCreate: $iob")
            } catch (e: Exception) {
                //log caught Exception
                CrashReporter.exception(e)
            }
        }

        Log.e(tag, "onCreate: ${getCrashReportPath()}")

        findViewById<Button>(R.id.btnNullPointer).setOnClickListener {
            try {
                context.resources
            } catch (e: java.lang.Exception) {
                CrashReporter.exception(e)
            }
        }

        findViewById<Button>(R.id.btnClassCast).setOnClickListener {
            try {
                val x: Any = 0
                println(x as String)
            } catch (e: java.lang.Exception) {
                CrashReporter.exception(e)
            }
        }

        findViewById<Button>(R.id.btnArray).setOnClickListener {
            try {
                val x: Array<String?> = arrayOfNulls(3)
                x[-1]
            } catch (e: java.lang.Exception) {
                CrashReporter.exception(e)
            }
        }

        findViewById<Button>(R.id.btnCheckLogs).setOnClickListener {
            startActivity(Intent(this, CrashLogActivity::class.java))
        }

    }

    private fun registerReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            crashReporterReceiver, IntentFilter(CRASH_REPORTER_LOCAL_EVENT)
        )
    }

    private fun unRegisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(crashReporterReceiver)
    }

    private var crashReporterReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val crashReports = intent.getStringExtra(CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY)
            print("Crash reports $crashReports")
            val isShowDialog =
                intent.getBooleanExtra(CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY, false)
            if (isShowDialog) {
                if (crashReports != null) {
                    print("Crash reports $crashReports")
                    val mDialogCrashReport = DialogCrashReport(this@MainActivity)
                    mDialogCrashReport.setCancelable(true)
                    mDialogCrashReport.setError(crashReports)
                    mDialogCrashReport.setNegativeButton("Cancel") { mDialogCrashReport.dismiss() }
                    mDialogCrashReport.show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceivers()
    }

    override fun onStop() {
        super.onStop()
        unRegisterReceivers()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceivers()
    }
}