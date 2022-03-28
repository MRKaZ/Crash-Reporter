package com.mrkazofficial.crashreporter.adapters

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mrkazofficial.crashreporter.R
import com.mrkazofficial.crashreporter.adapters.CrashLogsAdapter.AdapterHolder
import java.io.File
import java.io.InputStream

/**
 * @Project Crash Reporter
 * @Class Adapter
 * @Author MRKaZ
 * @Since 6:50 PM, 3/9/2022
 * @Origin Taprobana (LK)
 * @Copyright (c) 2022 MRKaZ. All rights reserved.
 */

@SuppressLint("NotifyDataSetChanged")
internal class CrashLogsAdapter : RecyclerView.Adapter<AdapterHolder>() {

    private lateinit var mContext: Context
    private var fileArrayList = ArrayList<File>()

    fun setupList(fileArrayList: ArrayList<File>) {
        this.fileArrayList = fileArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        this.mContext = parent.context
        return AdapterHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.crash_log_item_row, parent, false)
        )
    }


    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {

        holder.txtFileName.text = fileArrayList[position].name
        //holder.txtFileSize.text = fileArrayList[position].name
        holder.txtContent.text =
            bufferReaderFromFile(
                fileArrayList[position]
            )

        //holder.expandableLayout.initLayout()
        holder.expandableLayout.collapse()

        if (holder.expandableLayout.isExpanded)
            holder.expandableLayout.collapse()

        holder.expandableSpinner.setOnClickListener {
            holder.expandableLayout.toggle()
            if (holder.expandableLayout.isExpanded) {
                holder.imgExpandButton.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                holder.imgExpandButton.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }

        holder.txtContent.setOnClickListener {
            setClipboard(mContext, holder.txtContent.text.toString())
            Toast.makeText(mContext, "Report copied.", Toast.LENGTH_SHORT).show()
        }

        holder.adapterContainer.setOnClickListener {

            val items = arrayOf<CharSequence>(
                "Copy", "Delete", "Cancel"
            )

            MaterialAlertDialogBuilder(mContext)
                .setTitle("Choose")
                .setItems(items) { dialog: DialogInterface?, which: Int ->
                    when {
                        items[which] == "Copy" -> {
                            setClipboard(mContext, holder.txtContent.text as String)
                            Toast.makeText(mContext, "Copied.", Toast.LENGTH_SHORT).show()
                        }
                        items[which] == "Delete" -> {
                            if (this.fileArrayList.size != 0) {
                                this.fileArrayList[position].delete()
                                this.fileArrayList.removeAt(position)
                                notifyItemRemoved(position)
                                notifyDataSetChanged()
                            }
                        }
                        else -> {
                            dialog!!.dismiss()
                        }
                    }
                }
                .create()
                .show()
        }
    }

    override fun getItemCount(): Int {
        return fileArrayList.size
    }

    internal inner class AdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFileName: TextView = itemView.findViewById(R.id.txtFileName)

        //val txtFileSize: TextView = itemView.findViewById(R.id.txtFileSize)
        val txtContent: TextView = itemView.findViewById(R.id.txtContent)
        val expandableSpinner: RelativeLayout = itemView.findViewById(R.id.expandableSpinner)
        val expandableLayout: ExpandableRelativeLayout =
            itemView.findViewById(R.id.expandableLayout)
        val imgExpandButton: ImageView = itemView.findViewById(R.id.imgExpandButton)
        val adapterContainer: LinearLayout = itemView.findViewById(R.id.adapterContainer)
    }

    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
        } else {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("", text)
            clipboard.setPrimaryClip(clip)
        }
    }

    private fun bufferReaderFromFile(file: File?): String {
        val inputStream: InputStream = File(file!!.absolutePath).inputStream()
        return inputStream.bufferedReader().use { it.readText() }
    }
}