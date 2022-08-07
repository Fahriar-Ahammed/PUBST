package com.app.pub.dialogue

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.widget.TextView
import android.graphics.drawable.ColorDrawable
import com.app.pub_st.R

class SuccessDialogue(private val activity: Activity, private val mtext: String) {
    private var alertDialog: AlertDialog? = null
    fun startLoadingDialogue() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.success_dialogue, null)
        builder.setView(view)
        val textView = view.findViewById<TextView>(R.id.success_tex)
        textView.text = mtext
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
        alertDialog!!.show()
    }

    fun dismissDialogue() {
        alertDialog!!.dismiss()
    }
}