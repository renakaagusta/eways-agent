package com.eways.agent.core.baseactivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog


abstract class BaseActivity: AppCompatActivity() {
    private var pDialog: SweetAlertDialog? = null

    open fun showProgress() {
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog!!.titleText = "Memuat"
        pDialog!!.setCancelable(false)
        pDialog!!.show()
    }

    open fun dismissProgress() {
        if (pDialog != null) pDialog!!.dismiss()
    }

    open fun showSuccess(message: String?) {
        pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        pDialog?.titleText = "Sukses"
        pDialog?.contentText = message
        pDialog?.setConfirmClickListener {
            it.dismiss()
        }
        pDialog?.show()
    }

    open fun showError(text: String) {
        pDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        pDialog?.titleText = "Error"
        pDialog?.contentText = text
        pDialog?.setConfirmClickListener {
            it.dismiss()
        }
        pDialog?.show()
    }


    open fun showWarning(text: String) {
        pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog?.titleText = "Warning"
        pDialog?.contentText = text
        pDialog?.setConfirmClickListener {
            it.dismiss()
        }
        pDialog?.show()
    }

    open fun setConfirm(onConfirm:() -> Unit) {
        pDialog?.setConfirmClickListener {
            onConfirm()
            pDialog?.dismiss()
        }
    }
    
}