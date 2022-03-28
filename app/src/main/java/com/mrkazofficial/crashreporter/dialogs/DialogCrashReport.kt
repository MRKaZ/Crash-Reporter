package com.mrkazofficial.crashreporter.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.aakira.expandablelayout.ExpandableLinearLayout
import com.mrkazofficial.crashreporter.R

/**
 * @author MRKaZ
 * @since 12:28 AM, 5/16/2021, 2021
 */
class DialogCrashReport @SuppressLint("InflateParams") constructor(mContext: Context) {
    private val mDialog: Dialog
    private val expandableLayout: ExpandableLinearLayout
    fun show() {
        mDialog.show()
    }

    fun dismiss() {
        mDialog.dismiss()
    }

    fun setCancelable(setCancelable: Boolean) {
        mDialog.setCancelable(setCancelable)
    }

    fun setError(mTitle: String?) {
        val setMessage = mDialog.findViewById<TextView>(R.id.detail_error)
        if (setMessage != null) {
            setMessage.visibility = View.VISIBLE
            setMessage.text = mTitle
        }
    }

    fun setNegativeButton(mTitle: String?, v: View.OnClickListener?) {
        val setNegativeButton = mDialog.findViewById<Button>(R.id.btnNegative)
        if (setNegativeButton != null) {
            setNegativeButton.visibility = View.VISIBLE
            setNegativeButton.setOnClickListener(v)
            setNegativeButton.visibility = View.VISIBLE
            if (mTitle != null) {
                setNegativeButton.text = mTitle
            }
        }
    }

    init {
        val mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        mWindowManager.defaultDisplay.getMetrics(displayMetrics)
        val mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_crash_report, null)

        val screenWidth: Int = displayMetrics.widthPixels
        // screenHeight = displaymetrics.heightPixels;

        mView.minimumWidth = screenWidth

        mDialog = Dialog(mContext, R.style.BaseTheme_Dialog_Crash)
        mDialog.requestWindowFeature(1) // FEATURE_NO_TITLE = 1
        mDialog.setContentView(mView)
        mDialog.window!!.setGravity(Gravity.CENTER) // Gravity.CENTER = int 17
        val mWindowManagerLayout = mDialog.window!!.attributes
        mWindowManagerLayout.x = 0
        mWindowManagerLayout.y = 0
        mDialog.window!!.attributes = mWindowManagerLayout
        val mImageViewExpand = mDialog.findViewById<ImageView>(R.id.expand_button)
        // Setup expandable layout // Make sure cast the ExpandableLayout
        expandableLayout =
            mDialog.findViewById<View>(R.id.expandable_layout) as ExpandableLinearLayout
        mImageViewExpand.setOnClickListener {
            expandableLayout.toggle()
            if (expandableLayout.isExpanded) {
                mImageViewExpand.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                mImageViewExpand.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
    }
}