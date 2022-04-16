package com.proyek.infrastructures.utils

import android.content.Context
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog

class SweetAlert {
    private lateinit var progress: SweetAlertDialog
    private lateinit var error: SweetAlertDialog
    private lateinit var success: SweetAlertDialog

    fun showProgress(context: Context) {
        progress = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        progress!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        progress!!.titleText = "Memuat"
        progress!!.setCancelable(false)
        progress.show()
    }

    fun dismissProgress() {
        progress.dismiss()
    }

    fun showError(context: Context, text: String) {
        val error = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        error.titleText = "Error"
        error.contentText = text
        error.show()
    }
}