package com.app.pub_st.dialogue

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.app.pub_st.R

class LoadingDialogue(private val activity: Activity) {
    private var alertDialog: AlertDialog? = null
    fun startLoadingDialogue() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.loading_dialogue, null)
        builder.setView(view)
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