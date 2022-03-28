package com.mrkazofficial.crashreporter

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mrkazofficial.crashreporter.adapters.CrashLogsAdapter
import com.mrkazofficial.library.task.CrashReporter
import com.mrkazofficial.library.utils.Constants.LOG_FILE_EXTENSION
import com.mrkazofficial.library.utils.CrashReporterUtils
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("NotifyDataSetChanged")
class CrashLogActivity : AppCompatActivity() {

    private var fileList: ArrayList<File>? = null
    private var service: ExecutorService = Executors.newSingleThreadExecutor()
    private var handler: Handler = Handler(Looper.myLooper()!!)
    private lateinit var crashLogsAdapter: CrashLogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_log)

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        toolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.ic_close)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.title = "Crash log"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        val noCrashesLayout = findViewById<RelativeLayout>(R.id.noCrashesLayout)
        noCrashesLayout.visibility = View.GONE

        val recyclerViewCrashes = findViewById<RecyclerView>(R.id.recyclerViewCrashes)
        recyclerViewCrashes.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        crashLogsAdapter = CrashLogsAdapter()

        service.execute {
            fileList =
                ArrayList(CrashReporterUtils.getAllCrashes(File(CrashReporter.getCrashReportPath())))
            handler.post {
                fileList.let {
                    if (it != null) {
                        crashLogsAdapter.setupList(it)
                        crashLogsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        recyclerViewCrashes.adapter = crashLogsAdapter

        crashLogsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {

                if (CrashReporterUtils.getAllCrashes(File(CrashReporter.getCrashReportPath())).size == 0)
                    noCrashesLayout.visibility = View.VISIBLE
                else
                    noCrashesLayout.visibility = View.GONE

                if (fileList?.size!! > 0)
                    noCrashesLayout.visibility = View.GONE
                else
                    noCrashesLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun clearCrashLog() {
        if (CrashReporterUtils.getAllCrashes(File(CrashReporter.getCrashReportPath())).size > 0) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Clear crash logs")
                .setMessage("Are sure want to clear all crash logs?")
                .setPositiveButton("Yes") { dialog, _ ->
                    File(CrashReporter.getCrashReportPath()).listFiles()?.forEach {
                        if (it.absolutePath.endsWith(LOG_FILE_EXTENSION)) {
                            it.delete()
                            Toast.makeText(this, "Cleared.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if (this::crashLogsAdapter.isInitialized) {
                        fileList?.clear()
                        crashLogsAdapter.notifyDataSetChanged()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else
            Toast.makeText(this, "No crash logs to clear.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.log_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
        } else if (item.itemId == R.id.action_delete) {
            clearCrashLog()
        }
        return true
    }
}